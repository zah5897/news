var api_base_url     = 'http://180.150.184.207';
var weather_icon_base_url      = api_base_url + '/news';
var weather_api      = api_base_url + '/news/weather/now';
var news_baidu_api   = api_base_url + '/news/dao_hang/news_baidu';
var news_toutiao_api = api_base_url + '/news/dao_hang/news_toutiao';
var news_video_api   = api_base_url + '/news/dao_hang/videos';

function fetchWeather() {
	$.ajax({
		url        : weather_api,
		type       : 'post',
		cache      : false,
		dataType   : 'jsonp',
		jsonp      : "jsoncallback",
		contentType: "application/json;utf-8",
		success    : function(weather_data) {
			
			var weather_dom = '<a href="http://m.weather.com.cn" id="weather" class="mc weather_loading">\
				<span id="weather_temperature">' + weather_data.now.temperature + '°</span>\
				<img id="weather_icon" src="' + weather_icon_base_url + weather_data.now.weather_icon + '" alt="">\
				<span id="weather_city">' + weather_data.location.name + '</span>\
				<span id="weather_text">' + weather_data.now.text + '</span>\
				</a>';
			
			$('#weather_placeholder').html(weather_dom);
			
		},
		error      : function(jqXHR, textStatus, errorThrown) {
			
			console.log(textStatus);
			
		}
	});
}

function fetchBaiduNews() {
	$.ajax({
		url        : news_baidu_api,
		type       : 'get',
		data       : 'count=4',
		dataType   : 'jsonp',
		jsonp      : "jsoncallback",
		contentType: "application/json;utf-8",
		beforeSend : function() {
			
		},
		success    : function(news) {
			
			if (news && news.length > 0) {
				
				var topnews_html = '';
				
				for (var i = news.length - 1; i >= 0; i--) {
					
					topnews_html += '<li class="item"><a href="' + news[i]['url'] + '">' + news[i]['title'] + '</a></li>';
					
				}
				
				$('#hotNews').html(topnews_html).removeClass('hide');
				
			}
			
		},
		error      : function(jqXHR, textStatus, errorThrown) {
			
			console.log(textStatus);
			
		},
		complete   : function() {
			
		}
	});
	
}

function fetchSomeNews() {
	
	$.ajax({
		url        : news_toutiao_api,
		type       : 'get',
		data       : 'count=4',
		dataType   : 'jsonp',
		jsonp      : "jsoncallback",
		contentType: "application/json;utf-8",
		beforeSend : function() {
			
			$('#toutiaos').addClass('loading');
			
		},
		success    : function(news) {
			
			if (news && news.length > 0) {
				
				var news_html = '';
				
				for (var i = news.length - 1; i >= 0; i--) {
					
					news_html += '<dl class="toutiao" class="list">\
                      <dt class="thumb">\
                          <img src="' + news[i]['icon'] + '" alt="' + news[i]['title'] + '">\
                      </dt>\
                      <dd>\
                          <a href="' + news[i]['url'] + '" title="' + news[i]['title'] + '">' + news[i]['title'] + '</a>\
                      </dd>\
                  </dl>';
				}
				
				$('#toutiaos').html(news_html)
				
				$('#toutiao').removeClass('hide');
				
			}
			
		},
		error      : function(jqXHR, textStatus, errorThrown) {
			
			console.log(textStatus);
			
		},
		complete   : function() {
			
			$('#toutiaos').removeClass('loading');
			
		}
	});
	
}

function fetchSomeVideos() {
	$.ajax({
		url        : news_video_api,
		type       : 'post',
		dataType   : 'jsonp',
		jsonp      : "jsoncallback",
		contentType: "application/json;utf-8",
		beforeSend : function() {
			$('#videos').addClass('loading');
		},
		success    : function(rsp) {
			
			if (rsp && rsp.videos.length > 0) {
				
				var videos_html = '';
				
				for (var i = rsp.videos.length - 1; i >= 0; i--) {
					
					videos_html += '<a class="video" href="' + rsp.videos[i]['detail_html_url'] + '" title="' + rsp.videos[i]['id'] + '"><img class="thumb" src="' + rsp.videos[i]['preview_url'] + '" alt="' + rsp.videos[i]['id'] + '"><span class="title">' + rsp.videos[i]['title'] + '</span></a>';
				}
				
				$('#video_count').html(rsp.browse_count ? rsp.browse_count : 0);
				$('#videos').html(videos_html)
				$('#video').removeClass('hide')
			}
			
		},
		error      : function(jqXHR, textStatus, errorThrown) {
			
			console.log(textStatus);
			
		},
		complete   : function() {
			
			$('#videos').removeClass('loading');
			
		}
	});
	
}

$(function() {
	
	fetchWeather();
	
	fetchBaiduNews();
	
	fetchSomeNews();
	
	fetchSomeVideos();
	
});
