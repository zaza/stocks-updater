require 'win32ole'
require 'ftools'

module Updater

  class String
    def starts_with?(prefix)
      prefix = prefix.to_s
      self[0, prefix.length] == prefix
    end
  end

  def Updater.update(all_hash)
    now = DateTime::now()

    # create a backup copy first
    date = now.strftime("%Y%m%d") 
    File.copy("p:/docs/homebanking/portfel.xls", "p:/docs/homebanking/portfel" + date + ".xls")

    excel = WIN32OLE.new('Excel.Application')
    #excel.Visible = true
    #excel.Interactive = false
    #excel.ScreenUpdating = false

    workbook = excel.Workbooks.Open("p:/docs/homebanking/portfel.xls")

    WIN32OLE.codepage = WIN32OLE::CP_UTF8

    # update "historia" worksheet
    ws = workbook.Worksheets('historia')

    cell_data = ws.Cells(2,1)
    cell_razem = ws.Cells(2,2)

    ws.Rows(3).Insert

    ws.Cells(3,1).Value = cell_data.Value
    cell_data.Value = now.year.to_s + "-" + now.month.to_s + "-" + now.day.to_s

    ws.Cells(3,2).Value=cell_razem.Value
    ws.Cells(3,3).Formula = "=(B3-B4)/B3"
    ws.Cells(2,3).Formula = "=(B2-B3)/B2"

    # update "PORTFEL" worksheet
    ws = workbook.Worksheets('PORTFEL')

    all_hash.each {|key, value| 
      find_and_update(ws, key, value)
    }

    #excel.ScreenUpdating = true
    
    workbook.Save
    workbook.Close
    excel.Quit

    puts "Done."
  end

  def Updater.find_and_update(ws, key, item)
    print "Looking for '" + key + "'... "
    $\ = "\n"

    v = ""
    found = false
    150.downto(1) {|i|
      # column C
      v = ws.Cells(i, 3).Value
      # [<short name>] <long name> (sr) 
      if v =~ /\[([A-Z0-9]+)\].+\(sr\)/
        if $1 == key
          update_cell("1", ws, i.to_s, item)
          found = true
          break
        end
        # <long name> (sr)
      elsif v =~ /([\w\ \.:ęóąśłżźćńĘÓĄŚŁŻŹĆŃ]+)\ \(sr\)/
        if $1 == key
          update_cell("2", ws, i.to_s, item)
          found = true
          break
        end
        # [<short name>] <long name>
      elsif v =~ /\[([A-Z0-9]+)\].*/
        if $1 == key
          update_cell("3", ws, i.to_s, item)
          found = true
          break
        end
        # <long name>
      elsif v =~ /[\w\ \.:ęóąśłżźćńĘÓĄŚŁŻŹĆŃ-]+/
        if $& == key
          update_cell("4", ws, i.to_s, item)
          found = true
          break
        end
      end
    }
    if (!found)
      p "NOT FOUND!"
      $\ = nil
    end
  end
  
  def Updater.update_cell(type, ws, row, item)
    oldValue = ws.Range("F" + row).Value
    ws.Range("F" + row).Value = item.value
    ws.Range("G" + row).Value = item.date
    newValue = new_value(item.value)
    change = (newValue / oldValue.to_f-1) * 100
    printf("(%s) found at %s, change %.2f%\n", type, row, change)
    $\ = nil
  end

 def Updater.new_value(value)
    # TODO: replace with start_with?('=') available in 1.9
    if value =~ /\A=/
      value = value[1, value.length]
    end

    # replace % with float
    i, op, j = value.scan(/([\d,]+)([+\-*\/])?([\d+,]+\%?)?/)[0] #=> ["4037.83", "*", "95%"]
    if  j =~ /\%$/
      j = "0." + j[0..-2]
      result = i.to_f.send op, j.to_f
    elsif !op.nil?
      i = i.gsub(",", ".")
      j = j.gsub(",", ".")
      result = i.to_f.send op, j.to_f
    else
      result = i = i.gsub(",", ".")
    end
    return result.to_f
  end

end #module