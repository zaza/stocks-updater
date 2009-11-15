class Item
  @@print_names = false
  def Item.print_names(p)
    @@print_names = p
  end
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
    if @@print_names
      "#{@name}\t#{@price}\t#{@date}"
    else  
      "#{@price}\t#{@date}"  
    end
  end 
end