class Item
  def initialize(n, p, d)
    @name = n
    @price = p
    @date = d
  end
  def price  
    @price  
  end  
  def date  
    @date  
  end  
  def to_s
    "#{@name}\t#{@price}\t#{@date}"
  end 
end