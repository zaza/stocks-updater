require 'win32ole'
require 'ftools'

module Updater  
  def Updater.update(all_hash)
    now = DateTime::now()
    
    # create a backup copy first
    File.copy("p:/docs/homebanking/portfel.xls", "p:/docs/homebanking/portfel"+now.year.to_s+now.month.to_s+now.day.to_s+".bak")
    
    excel = WIN32OLE.new('Excel.Application')
    #excel.Visible = true
    #excel.Interactive = false
    #excel.ScreenUpdating = false
    
    workbook = excel.Workbooks.Open("p:/docs/homebanking/portfel.xls")
    
    # update "historia" worksheet
    ws = workbook.Worksheets('historia')
    
    cell_data = ws.Cells(2,1)
    cell_razem = ws.Cells(2,2)
    cell_tomek = ws.Cells(2,3)
    
    ws.Rows(3).Insert
    
    ws.Cells(3,1).Value = cell_data.Value
    cell_data.Value = now.year.to_s + "-" + now.month.to_s + "-" + now.day.to_s
    
    ws.Cells(3,2).Value=cell_razem.Value
    ws.Cells(3,3).Value=cell_tomek.Value
    ws.Cells(3,4).Formula = "=(B3-B4)/B3"
    ws.Cells(2,4).Formula = "=(B2-B3)/B2"
    
    # update "PORTFEL" worksheet
    ws = workbook.Worksheets('PORTFEL')
    
    all_hash.each {|key, value| 
      find_and_update(ws, key, value)
    }
=begin
    i = all_hash["Investor FIZ"]
    ws.Range("F5").Value = i.price
    ws.Range("G5").Value = i.date    
    
    i = all_hash["Investor Gold FIZ"]
    ws.Range("F8").Value = i.price
    ws.Range("G8").Value = i.date 

    i = all_hash["USD"]
    ws.Range("F16").Value = i.price
    ws.Range("G16").Value = i.date    
    
    i = all_hash["AUD"]
    ws.Range("F19").Value = i.price
    ws.Range("G19").Value = i.date    
=end    
    i = all_hash["Amplico SFIO Parasol Świat. Sub. Akcji Chińskich i Azj."]
    ws.Range("F34").Value = i.price
    ws.Range("G34").Value = i.date
    
    i = all_hash["Legg Mason Akcji FIO"]
    ws.Range("F38").Value = i.price
    ws.Range("G38").Value = i.date
    
    i = all_hash["PZU FIO Akcji MiŚ Spółek"]
    ws.Range("F41").Value = i.price
    ws.Range("G41").Value = i.date
    
    i = all_hash["UniFundusze FIO Sub. UniAkcje MiŚS"]
    ws.Range("F42").Value = i.price
    ws.Range("G42").Value = i.date
    
    i = all_hash["UniFundusze FIO Sub. UniAkcje: Polska 2012"]
    ws.Range("F43").Value = i.price
    ws.Range("G43").Value = i.date
=begin    
    i = all_hash["ARKAFRN12"]
    ws.Range("F47").Value = i.price
    ws.Range("G47").Value = i.date
    
    i = all_hash["BMP"]
    ws.Range("F50").Value = i.price
    ws.Range("G50").Value = i.date
    
    i = all_hash["COG"]
    ws.Range("F53").Value = i.price
    ws.Range("G53").Value = i.date
    
    i = all_hash["INK"]
    ws.Range("F56").Value = i.price
    ws.Range("G56").Value = i.date
    
    i = all_hash["IPL"]
    ws.Range("F57").Value = i.price
    ws.Range("G57").Value = i.date
    
    i = all_hash["KGH"]
    ws.Range("F58").Value = i.price
    ws.Range("G58").Value = i.date
    
    i = all_hash["RHD"]
    ws.Range("F59").Value = i.price
    ws.Range("G59").Value = i.date
    
    i = all_hash["VST"]
    ws.Range("F63").Value = i.price
    ws.Range("G63").Value = i.date
    
    i = all_hash["ZAP"]
    ws.Range("F64").Value = i.price
    ws.Range("G64").Value = i.date
    
    i = all_hash["RCCRUAOPEN"]
    ws.Range("F65").Value = i.price
    ws.Range("G65").Value = i.date
=end
    
    #excel.ScreenUpdating = true
    
    workbook.Save
    workbook.Close
    excel.Quit
    
    puts "Done."
  end  
  def Updater.find_and_update(ws, key, item)
    v = ""
    100.downto(1) {|i|
      # column C
      v = ws.Cells(i, 3).Value
      # [ticker] xxx (sr) 
      if v =~ /\[([A-Z0-9]+)\].+\(sr\)/
        if $1 == key
          #          p key + "-> C"+i.to_s
          ws.Range("F" + i.to_s).Value = item.price
          ws.Range("G" + i.to_s).Value = item.date
          break
        end
        # xxx (sr)
      elsif v =~ /([\w\ ]+)\ \(sr\)/
        if $1 == key
          #          p key + "-> C"+i.to_s
          ws.Range("F" + i.to_s).Value = item.price
          ws.Range("G" + i.to_s).Value = item.date
          break
        end        
        # [ticker] xxx
      elsif v =~ /\[([A-Z0-9]+)\].*/
        if $1 == key
          #          p key + "-> C"+i.to_s
          ws.Range("F" + i.to_s).Value = item.price
          ws.Range("G" + i.to_s).Value = item.date
          break
        end
        # xxx
      elsif v =~ /[\w\ ]+/
        if $& == key
          #          p key + "-> C"+i.to_s
          ws.Range("F" + i.to_s).Value = item.price
          ws.Range("G" + i.to_s).Value = item.date
          break
        end
      end
    }
  end
end
