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

facebook放入 http://127.0.0.1 正在實驗中
可是這樣怎麼拿到前端呢?傷腦筋,再說吧




todo

UI
	畫面
		整個畫面置中
		還不能看
		CALMAP字眼拿掉
	REFRESH機制
		過一陣子就清除所有CACHE內容,讓程式直接從SERVER中讀取資料,不然實在累積太多資料啦
	項目在不同tab之間移動
		目前fav還沒有移動到loc的內容
		目前某個TAB中項目可能太多,導致呈現太慢,要有一個呈現上限
		要加入不想去TAB
		>>>>要加入想去TAB<<<<
		速度有點慢
	同一個TAB之間移動順序
		要如何紀錄進系統呢?
	地點TAB
		出來項目要標記這是著名,熱門,一般,  系統推薦(TASTE) 朋友去過推薦  文字內容推薦
		如果被過濾太多,導致畫面內容太少,應該會啟動重新繪製一次
		光復路全部移除完畢後,畫面變成很高LEVEL的中國??
	客戶登入機制
		facebook
			時有時無
			error handle
			已經登入成功,但是受限於順序問題,還是使用guest
			如果沒有登入過,就用cookie使用
				直到登入了,就將cookie內容rename成為name資料
			為什麼我的facebook帳號不能取得friend資料呢
			facebook那邊如何確認localhost和server都可以運作呢
		google
			http://www.died.tw/2010/07/google-openid.html
		self db system
	MAP
		點選畫面上的點,應該會是打開目前TAB的某個項目,讓他打開
	詳細內容
		出現詳細內容時,最好有一張很大圖片,比照trazzler
		要加入縮圖,縮圖可以考慮放在自己server中,避免檔案不見
	搜尋
		先不要搜尋,就算是搜尋,可能把結果cache在file中
	misc
		compile實在太慢,是不是多用了什麼import宣告
		小舅的地圖MERGE,似乎很有趣
	被推薦前面幾個,要順便顯示簡單描述
	新做出來的項目,要有個QUEUE,自動更新GEODATA
	目前在其他TAB沒辦法把東西移去其他TAB
	刪除所有東西,會變成預設畫面
	切換tab時候速度太慢,受不了
	點選地圖上的點,會打開TAB裡面隱藏HTML
	接近住家附近,晃來晃去,就會看到地圖上很多景點,但是在LOCATION上面沒有對應項目,可能是地圖上的點沒有移走??
	加入fav和不想去的兩個tab
	菁山遊憩區 娟絲瀑布 草山行館
		沒有評論資料,不太可能啊
	文化大學都是上課資訊,要保留嗎
	>>>>>加入小圖片,大圖片<<<<<
	要加上資料從哪裡來的
	台北市教師研習中心為什麼出現兩次
	instance的生成,最好是真正需要才生成,類似bio的做法
	點項目要連到地圖,或是說,打開來可以看到自己地圖,要怎麼做呢
	
SERVER
	cache機制
		先不需要加入時間機制,反正記憶體這麼多,人那麼少,能夠動,動的快為原則
	準備放在server上面長期使用
		單機可以測試
			開機確定可以執行WEB SERVER
			確定GROUP並沒有把其他IP檔掉
		自動讀取新的資料
			加入s3拷貝功能,能夠到s3裡面讀取新資料
			http://aws-musings.com/how-to-deploy-a-java-web-application-on-ec2-instance/
		目前DNS要移轉到新IP上
	aws config要能夠讀取
	讀取
		拿掉thisis1
		資料整合時,如果項目數量不夠,就擴大抓取範圍重新查詢一次
		不需要每次讀取s3數值,放在local filesystem當作cache,過一陣子自動更新
	>>>>>>ip2geo<<<<<<
		讀取新資料
	misc
		十月初,測試能不能取得pixiu,waggler

offline parser
	這個可能放在parser部分
	製作一個流程,讀取小檔案
	更新大檔案
	進行推薦更新,計算,更新檔案

>>>>>UI一次出來太多了,有點誇張<<<<<
>>>>>目前facebook專案名稱太難聽<<<<<
>>>>>目前facebook我的帳號不能列出朋友??<<<<<