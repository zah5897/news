function fetchWeather(){
	$.ajax( {  
      url:'http://117.143.221.190:8899/news/weather/now',
      type:'post',  
      cache:false,  
      dataType:'jsonp',  
      jsonp: "jsoncallback",
      contentType:"application/json;utf-8",
      success:function(weather_data) {  
        
        $('#weather_temperature').text(weather_data.now.temperature+'°');

        $('#weather_icon').attr('src','http://117.143.221.190:8899/news'+weather_data.now.weather_icon);

        $('#weather_city').text(weather_data.location.name);

        $('#weather_text').text(weather_data.now.text);

        $('#weather').removeClass('weather_loading');

      },  
      error : function(jqXHR,textStatus,errorThrown) {  
       
        console.log(textStatus);  

      }  
    }); 
}

function fetchSomeNews() {


        
$.ajax( {  
  url:'http://117.143.221.190:8899/news/dao_hang/news_toutiao',
  type:'get',   
  data:'count=4',
  dataType:'jsonp',  
  jsonp: "jsoncallback",
  contentType:"application/json;utf-8",
  beforeSend:function(){
  
    $('#toutiaos').addClass('loading');

  },
  success:function(news) {  
  

		if(news && news.length>0){

	  		var news_html =  '';
	  		var topnews_html = '';

	  		for (var i = news.length - 1; i >= 0; i--) {

topnews_html += '<li class="item"><a href="http://m.toutiao.com/'+ news[i]['url'] +'">'+ news[i]['title'] +'</a></li>';

	  			news_html += '<dl class="toutiao" class="list">\
                    <dt class="thumb">\
                        <img src="'+ news[i]['icon'] +'" alt="'+ news[i]['title'] +'">\
                    </dt>\
                    <dd>\
                        <a href="http://m.toutiao.com/'+ news[i]['url'] +'" title="'+ news[i]['title'] +'">'+ news[i]['title'] +'</a>\
                    </dd>\
                </dl>';
	  		}



	  		$('#toutiaos').html(news_html)

	  		$('#hotNews').html(topnews_html).removeClass('hide');


        $('#hotNews, #toutiao').removeClass('hide');

	  	}

  },  
  error : function(jqXHR,textStatus,errorThrown) {  
   
    console.log(textStatus);  

  },
  complete: function(){

    $('#toutiaos').removeClass('loading');

  }  
}); 

}


function fetchSomeVideos() {
	$.ajax( {  
	  url:'http://117.143.221.190:8899/news/dao_hang/videos',
	  type:'post',  
	  dataType:'jsonp',  
	  jsonp: "jsoncallback",
	  contentType:"application/json;utf-8",
    beforeSend:function(){
      $('#videos').addClass('loading');
    },
	  success:function(videos) {  
	  
	  	if(videos && videos.length>0){

	  		var videos_html =  '';

	  		for (var i = videos.length - 1; i >= 0; i--) {
	  		
	  			videos_html += '<a class="video" href="'+ videos[i]['detail_html_url'] +'" title="'+ videos[i]['id'] +'"><img class="thumb" src="'+ videos[i]['preview_url'] +'" alt="'+ videos[i]['id'] +'"><span class="title">'+ videos[i]['title'] +'</span></a>';
	  		}



	  		$('#videos').html(videos_html)
	  		$('#video').show()
	  	}
	  	
	  	

	  },  
	  error : function(jqXHR,textStatus,errorThrown) {  
	   
	    console.log(textStatus);  

	  },
  complete: function(){

    $('#videos').removeClass('loading');
    
  }  
	});
 
}



$(function(){


fetchSomeVideos();

fetchWeather();


fetchSomeNews();






    


})
