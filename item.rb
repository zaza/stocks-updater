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
  def value
    price
  end
  def to_s
    "#{@name}\t#{@price}\t#{@date}"
  end
end

class ItemWithModifier < Item
  def initialize(n, p, d, m)
    super(n, p, d)
    @modifier = m
  end
  def value
    "=" + @price.to_s + "*" + @modifier.to_s
  end
end