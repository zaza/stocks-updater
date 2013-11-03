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

def readlines(filename)
  result = []
  File.open(filename, "r").each_line do |line|
     result << line.chomp
  end
  return result
end

funds = readlines("funds.txt")
stooqs = readlines("stooqs.txt")
tickers = []
currencies = readlines("currencies.txt")

funds_hash = {}
stooqs_hash = {}
tickers_hash = {}
currencies_hash = {}
coins_hash = {}

if currencies.any?
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
#uri = URI.parse('https://www.walutomat.pl/')
#http = Net::HTTP.new(uri.host, uri.port)
#http.use_ssl = true if uri.scheme == "https"  # enable SSL/TLS
#http.verify_mode = OpenSSL::SSL::VERIFY_NONE
#http.start {
#  http.request_get(uri.path) {|res|
#    doc = Hpricot(res.body)
#    doc.search("//div[@id='obecny-kurs']/table/tr/td[@name='pair']/a").each do |a|
#    if a.inner_html =~ /([A-Z]{3})\ \/\ PLN/
#      if currencies.include?($1)
#        rate = a.parent.next_sibling.inner_html
#        price = Float(rate)
#        currencies_hash[$1] = Item.new($1, price.to_s.gsub("." , ","), now.strftime("%Y-%m-%d"))
#        #currencies.delete($1) #FIXME: don't look for it on baksy.pl
#      end
#    end
#  end
#  }
#}
#end

puts "Funds..."

if funds.any?
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
end

page = open("http://www.bankier.pl/inwestowanie/fundusze/narzedzia/profile/?uni_key=816").read
doc = Hpricot(page)
span = doc.at("/html/body/div[2]/div[3]/table/tr/td[2]/table[3]/tr[2]/td[2]/div/span")
if span.inner_html =~ /(\d+\.\d{2})/
  price = $1.gsub("." , ",")
  span = doc.at("/html/body/div[2]/div[3]/table/tr/td[2]/table[3]/tr[2]/td[2]/span")
  if span.inner_html =~ /[0-9]{4}-[0-9]{2}-[0-9]{2}/
     date = $&
     it = ItemWithModifier.new("UniDolar Obligacje USD", price, date, currencies_hash["USD"].price)
     funds_hash["UniDolar Obligacje USD"] = it
  end
end

puts "Stocks..."

if stooqs.any?
stooqs.each do |s|
  doc = Hpricot(open("http://stooq.com/q/?s="+s.downcase))
  doc.search("//span[@id='aq_"+s.downcase+"_c2|3']").each do |spanPrice|
    price = spanPrice.inner_html.gsub("." , ",")
    doc.search("//span[@id='aq_"+s.downcase+"_d2']").each do |spanDate|
      stooqs_hash[s] = Item.new(s, price, spanDate.inner_html)
    end
  end
end
end

if tickers.any?
doc = Hpricot(open("http://www.bankier.pl/inwestowanie/notowania/akcje.html"))
doc.search("//tr[@id='noto']").each do |tr_noto|
  ticker = tr_noto.search("/td")[1].inner_html
  if tickers.include?(ticker)
    price = tr_noto.search("/td")[2].inner_html
    if price =~ /[0-9]+\.[0-9]{2}/
      price = $&.gsub("." , ",")
      date = tr_noto.search("/td")[10].inner_html
      if date =~ /[0-9]{2}-[0-9]{2}/
        date = $&
        tickers_hash[ticker] = Item.new(ticker, price, now.year.to_s + "-" + date)
      end
    end
  end
end
end

puts "Coins..."

page = open("http://zlotyranking.pl/zloto_live.txt").read
#span = doc.at("//span[@id='cenazl']")
if page =~ /"ts":(\d+)\}/
  ts = $1
  time = Time.at(ts.to_i)
  page = open("http://zlotyranking.pl/ceny-srebra").read
  doc = Hpricot(page)
  div = doc.at("/html/body/div/div/div[2]/div/section/section/div[2]/div/div[@class='price']")
  if div.inner_html =~ /([0-9]+.[0-9]{2}) zł/
    price = $1.gsub("." , ",")
    date = time.strftime "%Y-%m-%d"
    it = Item.new("srebrne monety", price, date)
    coins_hash["srebrne monety"] = it
  end
end
end


uri = URI.parse('https://www.monety-inwestycyjne.pl/monety-zlote-1-oz-krugerrand-1oz-p-137.html')
http = Net::HTTP.new(uri.host, uri.port)
http.use_ssl = true if uri.scheme == "https"  # enable SSL/TLS
http.verify_mode = OpenSSL::SSL::VERIFY_NONE
http.start {
  http.request_get(uri.path) {|res|
    doc = Hpricot(res.body)
    form = doc.search("//form")[0]
    td = form.at("/table/tr/td/table/tr/td[2]")
    if td.inner_html =~ /(\d\.\d{3},\d{2})zl/
      price = $1.gsub("." , "")
      date = Date.today.to_s
      it = ItemWithModifier.new("Krugerrand", price, date, "95%")
      coins_hash["Krugerrand"] = it
    end
  }
}

funds_hash.each do |k, v| puts v end
stooqs.each { |i| puts stooqs_hash[i] }
tickers.each { |i| puts tickers_hash[i] }
currencies.each { |i| puts currencies_hash[i] }
puts coins_hash["srebrne monety"]
puts coins_hash["Krugerrand"]

print "Update Excel workbook [yN]: "
if gets.chomp == "y" then
  all_hash = {}
  all_hash = all_hash.merge(funds_hash)
  all_hash = all_hash.merge(stooqs_hash)
  all_hash = all_hash.merge(tickers_hash)
  all_hash = all_hash.merge(currencies_hash)
  all_hash = all_hash.merge(coins_hash)
  Updater.update(all_hash)
else
  print "Bye."
end
