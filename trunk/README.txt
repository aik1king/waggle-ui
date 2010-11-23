
GWT程式開發程序

首先要確認程式是要在GAE上面執行,還是自己TOMCAT
因為這會牽涉新開發程式時,選擇開發項目

	共同部分
	系統會自動產生XXX.gwt.xml ( XXX就是系統名稱 )
	內容格式固定
	<entry-point class='tw.kayjean.ui.client.Waggle_ui'/> 這是程式會自動尋找入口entrypoint的地方
	但是有幾個部份要由使用者增加
	<inherits name="com.google.gwt.maps.GoogleMaps" /> 專門配合地圖使用
	<script src="http://maps.google.com/maps?gwt=1&amp;file=api&amp;v=2.x&amp;key=ABQIAAAAh7YfkaBHi2ZeQWYxA00oABScZUigKRr9AyP7eqvVhM8r_5oX5BSIQQoDTw2oyYH2x2DWg1bcPMHu1Q" />
	這組KEY是配合idv使用
	<inherits name='com.allen_sauer.gwt.dnd.gwt-dnd'/> 專門配合拖拉使用
	如果有source需要這樣設定
	<source path='sdk'/>



自己TOMCAT
	<web-app>
	AAA 資料都加在這裡面
	</web-app>
	目前有四個可以使用
	<!-- Servlets -->
	<servlet-mapping>  先從URL變成服務名稱
	   <servlet-name>locationServlet</servlet-name>
	   <url-pattern>/waggle_ui/location</url-pattern>
	</servlet-mapping>
	<servlet>  再從服務名稱變成SERVER端指定物件
	   <servlet-name>locationServlet</servlet-name>
	   <servlet-class>tw.kayjean.ui.server.LocationsServiceImpl</servlet-class>
	</servlet>
	<!-- Default page to serve -->
	<welcome-file-list>
	   <welcome-file>Waggle_ui.html</welcome-file>
	</welcome-file-list>
	<listener>  這部份是系統,定時會把新加入部分DUMP出來,最好是直接存入系統
	   <listener-class>tw.kayjean.ui.server.StatisticsContextListener</listener-class>
	</listener>

使用GAE



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
google
	http://www.died.tw/2010/07/google-openid.html



todo

UI
	客戶登入機制facebook
		>>>>>>>>>一定要登入才能使用<<<<<<<<<
		facebook那邊如何確認localhost和server都可以運作呢
	畫面
		整個畫面置中
		還不能看
		CALMAP字眼拿掉
	登入
		使用者登入時,就會
			位置移動到他最專長的地方
			看到已經有一些朋友貢獻給他的各個內容
			引誘,讓他已是專家的地方出現在準備分配畫面上,期望能優先轉送出去
			>>>>>>>>>列出朋友區域<<<<<<<<<		
	REFRESH機制
		過一陣子就清除所有CACHE內容,讓程式直接從SERVER中讀取資料,不然實在累積太多資料啦
	項目在不同tab之間移動
		目前某個TAB中項目可能太多,導致呈現太慢,要有一個呈現上限
		要加入不想去TAB
		切換速度有點慢??
	地點TAB
		同一個TAB之間移動順序	要如何紀錄進系統呢?
		要加上資料從哪裡來的
		接近住家附近,晃來晃去,就會看到地圖上很多景點,但是在LOCATION上面沒有對應項目,可能是地圖上的點沒有移走??
		要有縮圖
		點項目要連到地圖,或是說,打開來可以看到自己地圖,要怎麼做呢
		如果被過濾太多,導致畫面內容太少,應該會啟動重新繪製一次
		光復路全部移除完畢後,畫面變成很高LEVEL的中國??
			刪除所有東西,會變成預設畫面
		詳細內容
			出現詳細內容時,最好有一張很大圖片,比照trazzler
			圖可以考慮放在自己server中,避免檔案不見
	MAP
		點選畫面上的點,應該會是打開目前TAB的某個項目,讓他打開
	misc
		目前facebook專案名稱太難聽,設定好像也有問題
		compile實在太慢,是不是多用了什麼import宣告
		instance的生成,最好是真正需要才生成,類似bio的做法
		小舅的地圖MERGE,似乎很有趣,其實可以變出地圖,當成附件傳送過去
SERVER立刻處理
	流程
		如果有登入過,從系統中讀出檔案(自己基本資料包括我的朋友LIST, 自己貢獻給朋友的項目, 別人貢獻給我出的項目 ),變成項目
		如果沒有登入過,直接開始增加一個項目
			同時,在S3鐘產生  自己基本資料包括我的朋友LIST
			cache紀錄開始建立時間
	cache機制
		先不需要加入時間機制,反正記憶體這麼多,人那麼少,能夠動,動的快為原則
		掃描機制已經可以運作
			加入保護機制,避免重複啟動
			如果都沒有變化
				就寫入檔案區(自己貢獻給朋友的項目)
				寫到別人內容,用QUEUE A,標記成 某人XXX ( 景點1 目前使用者 時間 .... )
				並且清除MEMORY內容
			如果有變化,繼續留著使用
	準備放在server上面長期使用
		>>>>>>>>單機可以測試<<<<<<<<
			開機確定可以執行WEB SERVER
			確定GROUP並沒有把其他IP檔掉
		自動讀取新的資料
			加入s3拷貝功能,能夠到s3裡面讀取新資料
			http://aws-musings.com/how-to-deploy-a-java-web-application-on-ec2-instance/
		>>>>>>>>目前DNS要移轉到新IP上<<<<<<<<
	aws config要能夠讀取
	資料部份
		拿掉thisis1
		不需要每次讀取s3數值,放在local filesystem當作cache,過一陣子自動更新
	定位
		ip2geo要完成
		讀取新資料
offline process
	geodata更新機制
	新做出來東西能夠更新geodata
	有個QUEUE A
		拿到 某人XXX ( 景點1 目前使用者 時間 .... )之後
			打開某人 別人貢獻給我出的項目  檔案,把目前資料加進去,某人接受的總數就會改變
				變更某人的預設位置
				取得某人的朋友LIST,寫入QUEUE
	有個QUEUE B
		拿到 某人XXX朋友A ( 某人XXX , 目前總數為A , 是什麼地方專家 )之後
			打開某人XXX朋友A 自己基本資料包括我的朋友LIST,找到 某人XXX 更新他的數字內容,還有專家內容
			


GEOCELL很多功能沒用到,可以刪除
ROUTE有個TYPE欄位還沒有使用
ROUTE加入資料時,沒有依照重要性加入,只是單純APPEND
現在放在TRASH上面,沒什麼反應耶,很奇怪,應該是寫法錯誤,要看新寫法比較適合
MAP和ROUTE加入時,要用完整名稱,移除時也能移除
經緯度傳遞,都用物件,不要用Y用X
打開一開始應該要顯示LOCATION PANEL那個,而不是空空的
SERVER CACHE機制加入保護
實際上線,如何LOG呢
刪除掉shared目錄

位置要換成
http://www.kayjean.idv.tw:8888/Waggle_ui.html?gwt.codesvr=127.0.0.1:9997
而且要修改
windows\system32\drivers\etc\hosts
這樣才能通過測試

公里海岸風景區,很奇怪,怎麼跑出來的
交大旁邊有玻璃館,很奇怪
點選詳細資料,找一個有詳細資料的列出來看看
