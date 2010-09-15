一定來不及放在SERVER的,只能從client下手
缺點是這個cache太大時,會有問題
不過,如果格子cache逐步更新,傳回來的數值和以往不同,而且屬於同一個TYPE的會自動消除,就會逐步減少內容
SERVER那邊,拿到一組之後,每個項目只保留最後一個,和先前合併比較
一陣計算後,會更新許多數值
資料應該會放在local,過一陣子會刪除舊資料,讀取新資料



todo

UI
	畫面
		還不能看
	搜尋
		還不能搜尋
	項目移動不同tab
		目前fav還沒有移動到loc
		如果被過濾太多,導致畫面內容太少,怎麼辦呢?
		光復路全部移除完畢後,畫面變成很高LEVEL的中國??
		>>>>>>數量移動到某個程度後,推薦機制應該要啟動,調整地圖,吸引使用者點選詳細資料<<<<<
			出來項目要標記這是一般熱門 最近熱門 系統推薦 朋友去過推薦
	TAB
		目前某個TAB中項目可能太多,導致呈現太慢
		要加入刪除TAB
		要加入想去TAB
		要加入錯誤回報TAB
			新竹縣 新竹市 這種不用顯示啦
		TAB切換,繪圖的點要重新畫出
	移動map
		這部分可能要有timeout機制
		>>>>>cache機制似乎有錯誤<<<<<<<<
			新竹地區一直出現飛鳳山,但是明明已經跑去別的地方了
	客戶登入機制
		>>>>>>>已經登入成功,但是受限於順序問題,還是使用guest<<<<<<<<<<
		如果沒有登入過,就用cookie使用
			直到登入了,就切換
		登入之後,所有CACHE應該要清除,重新處理
		為什麼我的facebook帳號不能取得friend資料呢
		
SERVER
	準備放在server上面長期使用
		config要能夠讀取
		安裝新的ami
			可以安裝web apache/ tomcat/ 使用目前server
			安裝測試程式war
			寫入ami
			重新啟動ami
			將目前dns轉移到那個位置
			
	讀取
		不需要每次讀取s3數值,放在local filesystem當作cache,過一陣子自動更新
	ip2geo機制還沒完成

parser
	製作一個流程,讀取檔案
	更新大檔案
	進行index計算
	進行推薦更新,計算,更新檔案


MISC
	新竹縣 新竹市 這種不用顯示啦
	二水庫就是寶二水庫
	靜心湖和竹科靜心湖是一樣的東西啦
	道路名稱不要放進去
	國立交通大學和交通大學是一樣的東西啦
	
	系統很多經緯度不太正確情形,現在都草草處理,不太對啦
	
因為旭海找到這幾個地方,要加入
http://uukt.idv.tw/point.php?targMid=14
http://www.dotzing.com.tw/htm/spot/detail.php?sid=897
http://travel.network.com.tw/tourguide/point/showpage/298.html
http://www.easytravel.com.tw/guide2009_11/scenic.aspx?CityID=22&AreaID=322&PlaceID=2500


要加入縮圖
縮圖可以考慮放在自己server中
避免檔案不見


出現詳細內容時,最好有一張很大圖片,



1.把幾百個項目全部丟到CLIENT??不太可能
2.幾百個項目全部留在SERVER,不用全部掃描,只要讀取
目前接受LIST,各種LIST,對於畫圖有幫助嗎?
大概會採用目前架構,比較容易實行
或是說要