一定來不及放在SERVER的,只能從client下手
缺點是這個cache太大時,會有問題
不過,如果格子cache逐步更新,傳回來的數值和以往不同,而且屬於同一個TYPE的會自動消除,就會逐步減少內容
SERVER那邊,拿到一組之後,每個項目只保留最後一個,和先前合併比較
一陣計算後,會更新許多數值
資料應該會放在local,過一陣子會刪除舊資料,讀取新資料


流程很長,重要性來看
1.解決切身問題,找到附近可以去的地方
	雖然感覺很像是以前findory
2.最後在畫面上看到為原則,能夠操作為原則
3.最後再回頭看流程裡面  抓取,parser,ui,mining,還有舊的ui  等等相關

如果是GAE,優點很多,放在SERVER上就可以了
1.資料放上GAE的過程真的很麻煩
2.GAE不一定能順利使用在CHINA






todo

UI
	畫面
		還不能看
		要加入縮圖,縮圖可以考慮放在自己server中,避免檔案不見
	REFRESH機制
		過一陣子就清除所有CACHE內容,讓程式直接從SERVER中讀取資料,不然實在累積太多資料啦
	項目在不同tab之間移動
		目前fav還沒有移動到loc的內容
		移動map
			這部分可能要有timeout機制
			cache機制似乎有錯誤
				新竹地區一直出現飛鳳山,但是明明已經跑去別的地方了
	TAB
		目前某個TAB中項目可能太多,導致呈現太慢
		要加入刪除TAB
		要加入想去TAB
		要加入錯誤回報TAB
			新竹縣 新竹市 這種不用顯示啦
		TAB切換,繪圖的點要重新畫出
	地點TAB
		出來項目要標記這是著名,熱門,一般,  系統推薦(TASTE) 朋友去過推薦  文字內容推薦
		如果被過濾太多,導致畫面內容太少,應該會啟動重新繪製一次
		光復路全部移除完畢後,畫面變成很高LEVEL的中國??
	客戶登入機制
		facebook
			已經登入成功,但是受限於順序問題,還是使用guest
			如果沒有登入過,就用cookie使用
				直到登入了,就將cookie內容rename成為name資料
			為什麼我的facebook帳號不能取得friend資料呢
			facebook那邊如何確認localhost和server都可以運作呢
		google
			http://www.died.tw/2010/07/google-openid.html
		self
	詳細內容
		>>>>>>>>可以點選詳細資料<<<<<<<<
			點選的時候,SERVER會從S3取出檔案,包裝成資料結構,送到GWT
		出現詳細內容時,最好有一張很大圖片
	搜尋
		還不能搜尋

SERVER
	>>>>>>>>2cache機制<<<<<<<<
		確認完成,先不需要加入時間機制,反正記憶體這麼多,人那麼少,能夠動,動的快為原則
	>>>>>>>>1準備放在server上面長期使用<<<<<<<<
		http://aws-musings.com/how-to-deploy-a-java-web-application-on-ec2-instance/
		aws config要能夠讀取
		安裝新的ami
			可以安裝web apache/ tomcat/ 使用目前server
			安裝測試程式war
			寫入ami
			重新啟動ami
			將目前dns轉移到那個位置
		用一個IP接上去
			目前DNS要移轉到新IP上
	資料整合時,如果項目數量不夠,就擴大抓取範圍重新查詢一次
	讀取
		不需要每次讀取s3數值,放在local filesystem當作cache,過一陣子自動更新
	ip2geo機制還沒完成

offline parser
	製作一個流程,讀取小檔案
	更新大檔案
	進行推薦更新,計算,更新檔案

MISC
	新竹縣 新竹市 這種不用顯示啦
	二水庫就是寶二水庫
	靜心湖和竹科靜心湖是一樣的東西啦
	道路名稱不要放進去
	國立交通大學和交通大學是一樣的東西啦

	系統很多經緯度不太正確情形,現在都草草處理,不太對啦
	小舅的地圖MERGE,似乎很有趣
	
	十月初,測試能不能取得pixiu