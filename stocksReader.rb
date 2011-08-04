require 'rubygems'
require 'hpricot'
require 'open-uri'
require 'iconv'
require 'net/https'
require 'uri'

require 'item'
require 'updater'

puts "Fetching data. Please wait..."

now = DateTime::now()

funds = [
"Amplico SFIO Parasol Świat. Sub. Akcji Chińskich i Azj.",
"Legg Mason Akcji FIO",
"PZU FIO Akcji MiŚ Spółek",
"UniFundusze FIO Sub. UniAkcje MiŚS",
"UniFundusze FIO Sub. UniAkcje Sektory Wzrostu",
"UniFundusze FIO Sub. UniDolar Obligacje",
"Amplico FIO Parasol Kraj. Sub. Pieniężny",
"UniFundusze FIO Sub. UniKorona Pieniężny",
"Copernicus Dłużnych Papierów Korporacyjnych",
"Alternatywny"];
stooqs = [ "ARKAFRN12"]
tickers = [ "BMP", "INK", "JSW", "VST", "ZAP"]
currencies = [ "USD", "AUD", "EUR", "CHF" ]
investors = ["Investor FIZ", "Investor Gold FIZ"]

funds_hash = {}
stooqs_hash = {}
tickers_hash = {}
currencies_hash = {}
investors_hash = {}

puts "Funds..."

# only "(Skarbiec) Alternatywny"
page = open("http://www.skarbiec.pl/dla_klientow/notowania/").read
doc = Hpricot(page)
doc.search("//tr/td[@class='title']/span[@class='leftSide']/a").each do |a|
  fund = a.inner_html
  if fund == "Alternatywny"
    div = a.parent.parent.parent.at("td[@class='value']/div")
    if div.inner_html =~ /([0-9]+,[0-9]{2}) PLN/
      price = $1
      date = div.at("div[@class='data']").inner_html
      if date =~ /[0-9]{4}-[0-9]{2}-[0-9]{2}/
        date = $&
        funds_hash[fund] = Item.new(fund, price, date)
        break
      end
    end
  end
end

page = open("http://www.bankier.pl/inwestowanie/notowania/fundusze/?aktywny=7").read
page = Iconv.iconv('utf-8','iso-8859-2',page).first
doc = Hpricot(page)
doc.search("//tr/td/a[@class='atl']").each do |a|
  fund = a.inner_html
  if funds.include?(fund)
    price = a.parent.parent.search("/td")[2].inner_html
    if price =~ /[0-9]+(\.[0-9]{2})*/
      price = $&.gsub("." , ",")
      date = a.parent.parent.search("/td")[9].inner_html
      if date =~ /[0-9]{2}-[0-9]{2}/
        date = $&
        funds_hash[fund] = Item.new(fund, price, now.year.to_s + "-" + date)
      end
    end
  end
end

puts "Stocks..."

stooqs.each do |s|
  doc = Hpricot(open("http://stooq.com/q/?s="+s.downcase))
  doc.search("//span[@id='aq_"+s.downcase+"_c2|3']").each do |span|
    price = span.inner_html.gsub("." , ",")
    doc.search("//span[@id='aq_"+s.downcase+"_d2']").each do |span|
      stooqs_hash[s] = Item.new(s, price, span.inner_html)
    end
  end
end

doc = Hpricot(open("http://www.bankier.pl/inwestowanie/notowania/akcje.html"))
doc.search("//tr[@id='noto']").each do |tr|
  ticker = tr.search("/td")[1].inner_html
  if tickers.include?(ticker)
    price = tr.search("/td")[2].inner_html
    if price =~ /[0-9]+\.[0-9]{2}/
      price = $&.gsub("." , ",")
      date = tr.search("/td")[10].inner_html
      if date =~ /[0-9]{2}-[0-9]{2}/
        date = $&
        tickers_hash[ticker] = Item.new(ticker, price, now.year.to_s + "-" + date)
      end
    end
  end
end

puts "Currencies..."

doc = Hpricot(open("http://baksy.pl/kantor/kursy.php3"))
doc.search("//p[@class='std-b2']").each do |p|
  if p.inner_html =~ /\d{4}-\d{2}-\d{2}/
    date = $&
    doc.search("//tr/td[@class='lista']/nobr").each do |nobr|
      if nobr.inner_html =~ /([0-9 ]+)([A-Z]{3})/
        if currencies.include?($2)
          buy = nobr.parent.next_sibling.inner_html
          price = Float(buy)
          currencies_hash[$2] = Item.new($2, (price/100).to_s.gsub("." , ","), date)
        end
      end
    end
    break
  end
end

#TODO: run walutomat.pl first and skip found currencies on baksy.pl
uri = URI.parse('https://www.walutomat.pl/')
http = Net::HTTP.new(uri.host, uri.port)
http.use_ssl = true if uri.scheme == "https"  # enable SSL/TLS
http.verify_mode = OpenSSL::SSL::VERIFY_NONE
http.start {
  http.request_get(uri.path) {|res|
    doc = Hpricot(res.body)
    doc.search("//div[@id='obecny-kurs']/table/tr/td[@name='pair']/a").each do |a|
    if a.inner_html =~ /([A-Z]{3})\ \/\ PLN/
      if currencies.include?($1)
        rate = a.parent.next_sibling.inner_html
        price = Float(rate)
        currencies_hash[$1] = Item.new($1, price.to_s.gsub("." , ","), now.strftime("%Y-%m-%d"))
        #currencies.delete($1) #FIXME: don't look for it on baksy.pl
      end
    end
  end
  }
}

puts "Investors..."

doc = Hpricot(open("http://tfi.investors.pl/wyceny/fundusz.html"))
doc.search("//div[@id='main']/div[@id='content']/table[@id='fundfinder']/tr[@class='ffRow ']/td[@class='fndName']").each do |td|
  inv = td.at("a").inner_html
  if investors.include?(inv)
    nexttd = td.next_sibling.next_sibling
    if nexttd.inner_html =~ /(\d+,\d{2})&nbsp;zł/
    	price = $1
      #price = Float($1)
	    if nexttd.at("small").inner_html =~ /\d{4}-\d{2}-\d{2}/
  	  	date = $&
  	  	investors_hash[inv] = Item.new(inv, price, date)
  	 end
    end
  end
end

# monety:
#1) max(http://baksy.pl/zlom.php3 , http://www.acclamatio.pl/index.php)
#2) RCSILAOPEN = uncja srebra w pln

funds.each { |i| puts funds_hash[i] }
stooqs.each { |i| puts stooqs_hash[i] }
tickers.each { |i| puts tickers_hash[i] }
currencies.each { |i| puts currencies_hash[i] }
investors.each { |i| puts investors_hash[i] }

print "Update Excel workbook [yN]: "
if gets.chomp == "y" then
  all_hash = {}
  all_hash = all_hash.merge(funds_hash)
  all_hash = all_hash.merge(stooqs_hash)
  all_hash = all_hash.merge(tickers_hash)
  all_hash = all_hash.merge(currencies_hash)
  all_hash = all_hash.merge(investors_hash)
  Updater.update(all_hash)
else
  print "Bye."
end
