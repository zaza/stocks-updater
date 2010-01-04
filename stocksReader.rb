require 'rubygems'
require 'hpricot'
require 'open-uri'
require 'iconv'

require 'item'
require 'updater'

puts "Fetching data. Please wait..."

now = DateTime::now()

funds = [ 
"Amplico SFIO Parasol Świat. Sub. Akcji Chińskich i Azj.",
"Legg Mason Akcji FIO", 
"PZU FIO Akcji MiŚ Spółek",
"UniFundusze FIO Sub. UniAkcje MiŚS",
"UniFundusze FIO Sub. UniAkcje: Polska 2012",
"UniFundusze FIO Sub. UniDolar Obligacje"];
stooqs = [ "ARKAFRN12", "RCCRUAOPEN", "RCGLDAOPEN"]
tickers = [ "BMP", "COG", "INK", "IPL", "RHD", "VST", "ZAP" ]
currencies = [ "USD", "AUD" ]
investors = ["Investor FIZ", "Investor Gold FIZ"]

funds_hash = {}
stooqs_hash = {}
tickers_hash = {}
currencies_hash = {}
investors_hash = {}

page = open("http://www.bankier.pl/inwestowanie/notowania/fundusze/?aktywny=7").read
page = Iconv.iconv('utf-8','iso-8859-2',page).first
doc = Hpricot(page)
doc.search("//tr/td/a[@class='atl']").each do |a|
  fund = a.inner_html
  if funds.include?(fund)
    price = a.parent.parent.search("/td")[2].inner_html
    if price =~ /[0-9]+\.[0-9]{2}/
      price = $&.gsub("." , ",")
      date = a.parent.parent.search("/td")[9].inner_html
      if date =~ /[0-9]{2}-[0-9]{2}/
        date = $&
        funds_hash[fund] = Item.new(fund, price, now.year.to_s + "-" + date)
      end
    end
  end
end

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

doc = Hpricot(open("http://tfi.investors.pl"))
td = doc.at("//div[@id='assets_pricing']/table/tr[@class='first']/td[@class='r']")
if td.inner_html =~ /\d{4}-\d{2}-\d{2}/
  date = $&
  trs = doc.search("//div[@id='assets_pricing']/table/tr[@class='name']")
  for tr in trs
    inv = tr.at("/td[@class='l']").inner_html
    if investors.include?(inv)
      if tr.at("/td[@class='r']").inner_html =~ /\d+\.\d+/
        investors_hash[inv] = Item.new(inv, $&.gsub("." , ","), date)
      end
    end
  end
end

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
  p "Bye."
end