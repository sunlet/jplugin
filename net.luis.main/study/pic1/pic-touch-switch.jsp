<%@page pageEncoding="UTF-8"%>
<%@include file="/server/basic-m/header.jsp"%>

<div data-role="page" id="test"  class="ttt">
	<style>

.ttt{
	border:solid 2px red;
}

UL {
	PADDING-BOTTOM: 0px;
	MARGIN: 0px;
	PADDING-LEFT: 0px;
	PADDING-RIGHT: 0px;
	PADDING-TOP: 0px
}

.container {
	WIDTH: 100%;
	HEIGHT: 600px;
}

.container  IMG {
	WIDTH: 400px;
	HEIGHT: 300px
}

.container IMG {
	BORDER-BOTTOM-STYLE: none;
	BORDER-RIGHT-STYLE: none;
	BORDER-TOP-STYLE: none;
	BORDER-LEFT-STYLE: none
}

.csrcode {
	POSITION: absolute; //
	WIDTH: 90px; //
	FLOAT: right;
	TOP: 280px;
	LEFT: 110px
}

.csrcode LI {
	MARGIN: 10px;
	WIDTH: 20px;
	BACKGROUND:
		url(page_nofocus.png)
		no-repeat;
	FLOAT: left;
	HEIGHT: 20px;
	CURSOR: pointer
}

.csrcode LI.on {
	LINE-HEIGHT: 15px;
	WIDTH: 20px;
	BACKGROUND:
		url(page_focused.png)
		no-repeat;
	HEIGHT: 20px;
	COLOR: #ffffff
}
</style>
	<SCRIPT language=javascript>
		if (typeof (pgvMain) == 'function')
			pgvMain();
	</SCRIPT>
	<SCRIPT>
		var gtopTab = "one";
		function $id(id) {
			return document.getElementById(id);
		}
		function changesTab(tab_id) {
			if (tab_id == gtopTab) {
				return;
			} else {
				$id(gtopTab).className = "unselect";
				$id(tab_id).className = "select";
				$id("tab_" + gtopTab).style.display = "none";
				$id("tab_" + tab_id).style.display = "block";
				gtopTab = tab_id;
			}
		}
	</SCRIPT>
	<SCRIPT type=text/javascript>
		/*var $ = function(id) {
			return "string" == typeof id ? document.getElementById(id) : id;
		};*/
		var Extend = function(destination, source) {
			for ( var property in source) {
				destination[property] = source[property];
			}
			return destination;
		}
		var CurrentStyle = function(element) {
			return element.currentStyle
					|| document.defaultView.getComputedStyle(element, null);
		}
		var Bind = function(object, fun) {
			var args = Array.prototype.slice.call(arguments).slice(2);
			return function() {
				return fun.apply(object, args.concat(Array.prototype.slice
						.call(arguments)));
			}
		}
		var Tween = {
			Quart : {
				easeOut : function(t, b, c, d) {
					return -c * ((t = t / d - 1) * t * t * t - 1) + b;
				}
			},
			Back : {
				easeOut : function(t, b, c, d, s) {
					if (s == undefined)
						s = 1.70158;
					return c * ((t = t / d - 1) * t * ((s + 1) * t + s) + 1)
							+ b;
				}
			},
			Bounce : {
				easeOut : function(t, b, c, d) {
					if ((t /= d) < (1 / 2.75)) {
						return c * (7.5625 * t * t) + b;
					} else if (t < (2 / 2.75)) {
						return c * (7.5625 * (t -= (1.5 / 2.75)) * t + .75) + b;
					} else if (t < (2.5 / 2.75)) {
						return c * (7.5625 * (t -= (2.25 / 2.75)) * t + .9375)
								+ b;
					} else {
						return c
								* (7.5625 * (t -= (2.625 / 2.75)) * t + .984375)
								+ b;
					}
				}
			}
		}
		//容器对象,滑动对象,切换数量
		var SlideTrans = function(container, slider, count, options) {
			this._slider = $id(slider);
			this._container = $id(container);//容器对象
			this._timer = null;//定时器
			this._count = Math.abs(count);//切换数量
			this._target = 0;//目标值
			this._t = this._b = this._c = 0;//tween参数
			this.Index = 0;//当前索引
			this.SetOptions(options);
			this.Auto = !!this.options.Auto;
			this.Duration = Math.abs(this.options.Duration);
			this.Time = Math.abs(this.options.Time);
			this.Pause = Math.abs(this.options.Pause);
			this.Tween = this.options.Tween;
			this.onStart = this.options.onStart;
			this.onFinish = this.options.onFinish;
			var bVertical = !!this.options.Vertical;
			this._css = bVertical ? "top" : "left";//方向
			//样式设置
			var p = CurrentStyle(this._container).position;
			p == "relative" || p == "absolute"
					|| (this._container.style.position = "relative");
			this._container.style.overflow = "hidden";
			this._slider.style.position = "absolute";
			this.Change = this.options.Change ? this.options.Change
					: this._slider[bVertical ? "offsetHeight" : "offsetWidth"]
							/ this._count;
			//add by liuhang ,记录方向
			this.lastOffSet=0;
		};
		SlideTrans.prototype = {
			//设置默认属性
			SetOptions : function(options) {
				this.options = {//默认值
					Vertical : true,//是否垂直方向（方向不能改）
					Auto : false,//是否自动
					Change : 0,//改变量
					Duration : 10,//滑动持续时间
					Time : 10,//滑动延时
					Pause : 4000,//停顿时间(Auto为true时有效)
					onStart : function() {
					},//开始转换时执行
					onFinish : function() {
					},//完成转换时执行
					Tween : Tween.Quart.easeOut
				//tween算子
				};
				Extend(this.options, options || {});
			},
			//开始切换
			Run : function(index) {
				//修正index
				index == undefined && (index = this.Index);
				index < 0 && (index = this._count - 1) || index >= this._count
						&& (index = 0);
				//设置参数
				this._target = -Math.abs(this.Change) * (this.Index = index);
				this._t = 0;
				this._b = parseInt(CurrentStyle(this._slider)[this.options.Vertical ? "top"
						: "left"]);
				this._c = this._target - this._b;
				this.onStart();
				this.Move();
			},
			//移动
			Move : function() {
				clearTimeout(this._timer);
				//未到达目标继续移动否则进行下一次滑动
				if (this._c && this._t < this.Duration) {
					this.MoveTo(Math.round(this.Tween(this._t++, this._b,
							this._c, this.Duration)));
					this._timer = setTimeout(Bind(this, this.Move), this.Time);
				} else {
					this.MoveTo(this._target);
					this.Auto
							&& (this._timer = setTimeout(Bind(this, this.Next),
									this.Pause));

				}
			},
			//移动到
			MoveTo : function(i) {
				this._slider.style[this._css] = i + "px";
			},
			//偏移 // add by me
			MoveOffSet : function(offx, offy) {
				clearTimeout(this._timer);

				var pos = parseInt(CurrentStyle(this._slider)[this.options.Vertical ? "top"
						: "left"]);

				var newPos;
				if (this.options.Vertical) {
					newPos = pos + offy;
					this.lastOffSet = offy;
				} else {
					newPos = pos + offx;
					this.lastOffSet = offx;
				}
				this.MoveTo(newPos);
				//log(" offx = " + offx + " oldpos=" + pos + " newPos=" + newPos);
			},
			MoveOffSetEnd : function() {
				var poscss = $(this._slider).css(this._css);
				var pos = parseInt(poscss.substring(0, poscss.length - 1));
				var idx = 0;
				if (pos > 0) {
					idx = 0;
				} else {
					idx = Math.abs(pos) / this.Change;
					var ceil = Math.ceil(idx) ;
					var floor = Math.floor(idx);
					if (this.lastOffSet>0){
						
						if ( (ceil-idx) >0.1) {
							idx = floor;
						}else{
							idx = ceil;
						}
					}else{
						//alert("ceil="+ceil+" floor="+floor+" idx="+idx);
						if ((idx - floor) >0.03){
							idx = ceil;
						}else{
							idx = floor;
						}
					}
				}
				if (idx > (this._count - 1))
					idx = this._count - 1;
				this.Run(idx);
			},
			////////////////add end
			//下一个
			Next : function() {
				this.Run(++this.Index);
			},
			//上一个
			Previous : function() {
				this.Run(--this.Index);
			},
			//停止
			Stop : function() {
				clearTimeout(this._timer);
				this.MoveTo(this._target);
			}
		};
	</SCRIPT>

	<div data-role="content" style="margin:0;padding:0">
		<DIV id=idContainer2 class=container data-enhance="false">
			<TABLE id="idSlider2" border=0 cellSpacing=0 cellPadding=0
				style="width: 100%, height:100%">
				<TBODY>
					<TR>
						<TD class="td_f"><A href="#"><IMG src="wq1.png"></A></TD>
						<TD class="td_f"><A href="#"><IMG src="wq2.png"></A></TD>
						<TD class="td_f"><A href="#"><IMG src="wq3.png"></A></TD>
					</TR>
				</TBODY>
				<!-- <TBODY>
				<TR>
					<TD class="td_f"><A href="http://www.csrcode.net"><IMG
							src="http://www.CsrCode.cn/images/m01.jpg"></A></TD>
					<TD class="td_f"><A href="http://www.csrcode.net"><IMG
							src="http://www.CsrCode.cn/images/m02.jpg"></A></TD>
					<TD class="td_f"><A href="http://csrcode.net"><IMG
							src="http://www.CsrCode.cn/images/m03.jpg"></A></TD>
					<TD class="td_f"><A href="http://www.csrcode.net"><IMG
							src="http://www.CsrCode.cn/images/m04.jpg"></A></TD>
					<TD class="td_f"><A href="http://www.csrcode.net"><IMG
							src="http://www.CsrCode.cn/images/m05.jpg"></A></TD>
				</TR>
			</TBODY>
			 -->
			</TABLE>
			<UL id="www_csrcode_cn" class="csrcode"></UL>
		</DIV>

		<div id="output"></div>
		<SCRIPT>
			///////////////////////////////////////////////////////////
			var forEach = function(array, callback, thisObject) {
				if (array.forEach) {
					array.forEach(callback, thisObject);
				} else {
					for (var i = 0, len = array.length; i < len; i++) {
						callback.call(thisObject, array[i], i, array);
					}
				}
			}

			$("#test").on("pageinit",function() {
								var gh = $(window).height() - 1;
								var gw = $("#test").width() - 1;
								//alert(gh);
								$(".container").css("height", gh + "px");
								//alert($("img").length);
								$("img").css("height", gh + "px");
								$("img").css("width", gw + "px");
								//alert("init");

								var st = new SlideTrans("idContainer2",
										"idSlider2", 3, {
											Vertical : false,
											Change : gw
										});
								var nums = [];
								//插入数字
								for (var i = 0; i < st._count;i++) {
									(nums[i] = $id("www_csrcode_cn").appendChild(document.createElement("li")));
									$(nums[i]).attr("idx",i);
								}
								
								//计算ul位置
								var theul= $("ul");
								
								theul.css("left",(gw - (45 * st._count))/2 + "px" );
								theul.css("top",(gh-20-50) + "px" );
								
								$("ul").on("vmouseover","li",function(mevent){
									var o = mevent.currentTarget;
									var idx = $(o).attr("idx");
									//alert($(o).text());
									o.className = "on";
									st.Auto = false;
									st.Run(idx);
								});
								$("ul").on("vmouseout","li",function(mevent){
									var o = $(mevent.currentTarget).get(0);
									o.className = "";
									st.Auto = false;
									st.Run();
								});
								

								//设置按钮样式
								st.onStart = function() {
									forEach(
											nums,
											function(o, i) {
												o.className = st.Index == i ? "on"
														: "";
											})
								}
								st.Run();

								//////////////my code//////////////////////////
								var isdrag = false;
								var oldx, oldy;

								//$("#idContainer2").get(0).addEventListener("touchstart",
								$("#idContainer2").on("vmousedown",
										function(event) {
											//log("vmousedown down");
											isdrag = true;
											oldx = event.clientX;
											oldy = event.clientY;
											return false;
										});

								//$("#idContainer2").get(0).addEventListener("touchmove",
								$("#idContainer2").on("vmousemove",
										function(event) {
											//log("vmousemove");
											//alert("over");
											if (isdrag) {
												var newx, newy;
												newx = event.clientX;
												newy = event.clientY;
												var offx, offy;
												offx = newx - oldx;
												offy = newy - oldy;
												oldx = newx;
												oldy = newy;
												drag(offx, offy);
												//alert("drag");
											}
										});

								//$("#idContainer2").on("touchend", function(event) {
								$("#idContainer2").on("vmouseup",
										function(event) {
											//log("vmouseup");
											dragStop();
										});
								//$("#idContainer2").on("mouseout", function(event) {
								$("#idContainer2").on("vmouseout",
										function(event) {
											//log("vmouseout");
											dragStop();
										});

								function dragStop() {
									if (isdrag) {
										isdrag = false;
										st.MoveOffSetEnd();
										//alert("drag stop");
									}
								}
								function drag(offx, offy) {
									//alert("darging:"+offx+"  "+offy);
									st.MoveOffSet(offx, offy);
								}
								var idx = 0;
								function log(s) {
									$("#output").html(
											idx++ + s + "  isdrag=" + isdrag
													+ "<br>"
													+ $("#output").html());
								}
							});
		</SCRIPT>

	</div>
</div>