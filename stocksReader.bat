@echo off
call mvn clean package --quiet
java -cp target\stocks-updater-0.0.1-SNAPSHOT.jar;target\dependency\* com.github.zaza.stockreader.StockReader
rem install with 'scoop install ruby' followed by 'gem install ftools'
ruby stocksFileReader.rb v:\docs\homebanking\