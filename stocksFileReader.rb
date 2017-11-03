#! /usr/bin/env ruby
require './item'
require './updater'
require 'json'

all_hash = {}

string = File.open('all_hash.tmp', 'rb') { |f| f.read }
json = JSON.parse(string)
json.each do |m| 
	m.each do |k, v| 
		puts v
		if v["modifier"].nil?
			it = Item.new(v["name"], v["price"], v["date"])
		else
			it = ItemWithModifier.new(v["name"], v["price"], v["date"], v["modifier"])
		end
		all_hash[k] = it
	end
end

puts all_hash

print "Update Excel workbook [yN]: "
if STDIN.gets.chomp == "y" then
  Updater.update(all_hash)
else
  print "Bye."
end