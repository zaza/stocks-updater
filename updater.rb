require 'win32ole'
require 'ftools'

module Updater  
  def Updater.update(all_hash)
    now = DateTime::now()
    
    # create a backup copy first
    date = now.strftime("%Y%m%d") 
    File.copy("p:/docs/homebanking/portfel.xls", "p:/docs/homebanking/portfel" + date + ".bak")
    
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

    i = all_hash["Amplico SFIO Parasol Świat. Sub. Akcji Chińskich i Azj."]
    ws.Range("F35").Value = i.price
    ws.Range("G35").Value = i.date
    
    i = all_hash["Legg Mason Akcji FIO"]
    ws.Range("F39").Value = i.price
    ws.Range("G39").Value = i.date
    
    i = all_hash["PZU FIO Akcji MiŚ Spółek"]
    ws.Range("F42").Value = i.price
    ws.Range("G42").Value = i.date
    
    i = all_hash["UniFundusze FIO Sub. UniAkcje MiŚS"]
    ws.Range("F43").Value = i.price
    ws.Range("G43").Value = i.date
    
    i = all_hash["UniFundusze FIO Sub. UniAkcje: Polska 2012"]
    ws.Range("F44").Value = i.price
    ws.Range("G44").Value = i.date
    
    i = all_hash["UniFundusze FIO Sub. UniKorona Akcje"]
    ws.Range("F45").Value = i.price
    ws.Range("G45").Value = i.date    
    
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
          ws.Range("F" + i.to_s).Value = item.price
          ws.Range("G" + i.to_s).Value = item.date
          break
        end
        # xxx (sr)
      elsif v =~ /([\w\ ]+)\ \(sr\)/
        if $1 == key
          ws.Range("F" + i.to_s).Value = item.price
          ws.Range("G" + i.to_s).Value = item.date
          break
        end        
        # [ticker] xxx
      elsif v =~ /\[([A-Z0-9]+)\].*/
        if $1 == key
          ws.Range("F" + i.to_s).Value = item.price
          ws.Range("G" + i.to_s).Value = item.date
          break
        end
        # xxx
      elsif v =~ /[\w\ ]+/
        if $& == key
          ws.Range("F" + i.to_s).Value = item.price
          ws.Range("G" + i.to_s).Value = item.date
          break
        end
      end
    }
  end
end
