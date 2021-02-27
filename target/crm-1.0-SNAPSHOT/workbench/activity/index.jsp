<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

	<script type="text/javascript">

	$(function(){

		/*
			操作模态窗口的方式：
				需要操作的模态窗口的jquery对象，调用modal方法，向其中传递参数 show：展现   hide：关闭
		 */

		//  为了创建按钮绑定事件，打开添加操作的模态窗口
		$("#addBtn").click(function(){

			$("#create-owner").html("");
			// 日期插件
			$(".time").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "bottom-left"
			});

			// alert("=======");
			// $("#createActivityModal").modal("show");

			// 走后台，目的是为了取得用户信息列表，为所有者下拉框铺值
			$.ajax({
				url:"workbench/activity/getUserList.do",
				data:{
					// 无脑查所有
				},
				dataType:"json",
				success:function(data){

					/*

						data：需要从后台获取用户信息列表
						List<User> uList
						json = [
							{"zx","?","?"},
							{"ls","?",">"},
							...
						]

					 */
					var html = "<option></option>"

					// 遍历出来的每一个obj就是每一个用户的信息
					$.each(data, function(index, obj){

						html += "<option value='"+obj.id+"'>"+obj.name+"</option>";

					})

					$("#create-owner").append(html);

					// 取得当前用户的id
					// 在js中使用EL表达式，EL表达式一定要套在字符串中
					var defaultUUID = "${sessionScope.user.id}";

					$("#create-owner").val(defaultUUID);

					// 所有者展现完毕后展现模态窗口
					$("#createActivityModal").modal("show");

				},
				type:"get",
			})
		})

		// 为保存按钮绑定事件，执行添加操作
		$("#saveBtn").click(function(){

			$.ajax({
				url:"workbench/activity/save.do",
				data:{
					"owner":$.trim($("#create-owner").val()),
					"name":$.trim($("#create-name").val()),
					"startDate":$.trim($("#create-startDate").val()),
					"endDate":$.trim($("#create-endDate").val()),
					"cost":$.trim($("#create-cost").val()),
					"description":$.trim($("#create-description").val())
				},
				dataType:"json",
				type:"post",
				success:function(data){

					/*
						data:
							{"success":true/false}

					 */
					if(data.success){

						// 添加市场活动成功后
						// 刷新市场活动信息列表（局部刷新）

                        // 清空添加操作模态窗口中的数据
                        // 提交表单
                        // $("#activityAddForm").submit();
                        // $("#activityAddForm").reset();
                        /*

                            注意：
                                我们拿到了form表单的jquery对象
                                对于表单的jquery对象，提供了submit()方法让我们提交表单
                                但是表单的jquery对象，没有为我们提供reset()方法让我们重置表单

                                虽然form的jquery对象没有为我们提供reset方法，但是原先的js对象
                                当中有reset方法。我们可以将其转化为DOM对象调用该方法。

                                jquery对象转化为dom对象
                                    jquery对象[下标]

                                dom对象转换为jquery对象
                                    $(dom对象)
                        */
                        // var $objArray = $("#activityAddForm");
                        // var domObj = $objArray[0];
                        // domObj.reset();
                        $("#activityAddForm")[0].reset();

						// 关闭添加操作的模态窗口
						$("#createActivityModal").modal("hide");


					}else{

                        // 添加市场活动失败
						alert("添加市场活动失败");
					}

				}
			})

		});

		// 页面加载完毕后触发一个方法
		// 默认展开列表的第一页，每页展示两条数据
		pageList(1, 2);

		// 为查询按钮绑定事件，触发pageList方法
		$("#searchBtn").click(function(){
			/*

				点击查询按钮的时候，我们应该将搜索框中的信息保存起来（一般存放在隐藏域中）
			 */
			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-owner").val($.trim($("#search-owner").val()));
			$("#hidden-startDate").val($.trim($("#search-startDate").val()));
			$("#hidden-endDate").val($.trim($("#search-endDate").val()));
			pageList(1, 2);

		});

		// 为全选的复选框来绑定事件，触发全选操作
		$("#qx").click(function(){

			$(":input[name='xz']").prop("checked", this.checked);

		})

		// 实现反选：根据数据行选择标题行，触发全选操作
		/*
			一下操作是不能完成反选的功能的，因为动态生成的元素，是不能
			够以普通绑定事件的形式操作的
	 	*/
		// $(":input[name='xz']").click(function(){
		// })

		/*
			动态生成的元素，我们要以on方法的形式来触发事件
				语法：
					$(需要绑定元素的有效的外层元素).on(绑定事件的方式，需要绑定的元素的jquery对象，回调函数)

			什么是有效的外层元素呢？   不是该元素动态生成的外层元素就是有效的
		 */
		$("#activityBody").on("click",$(":input[name='xz']"),function(){
			// alert("some thing");
			$("#qx").prop("checked",$(":input[name='xz']").length==$("input[name=xz]:checked").length);
			// $("#qx").prop("checked",$(":input[name='xz']").length==$(":input[name='xz'][checked='true']").length);
		})

		// 为删除按钮绑定事件，执行市场活动的删除操作
		$("#deleteBtn").click(function(){

			// 找到复选框中所有挑勾儿的复选框的jquery对象
			var $xz = $("input[name=xz]:checked");

			if($xz.length == 0){

				alert("选择所要删除的记录");

			// 选择了，有可能是一条可能是多条
			}else{

				// 删除确认框
				if(confirm("确定要删除所选中的记录吗")){
// url:workbench/activity/delete.do?id=xxx&id=xxx&id=xxx

					// 拼接参数
					var param = "";

					// 将$xz中的每一个dom对象遍历出来，取其value值，就相当于取得了需要删除的记录的id
					for(var i = 0; i < $xz.length; i++){
						param += "id=" + $($xz[i]).val();

						// 如果不是最后一个元素，需要在后面追加一个&
						if(i < $xz.length - 1){

							param += "&";
						}

					}

					// 发出ajax
					$.ajax({
						url:"workbench/activity/delete.do",
						data:param,
						dataType:"json",
						type:"post",
						success:function(data){

							/*
                                {"success":true}  || {"success":false}
                             */
							if(data.success){
								alert("成功");
								pageList(1, 2);

							}else{
								alert("失败");
							}
						}
					})

				}


			}
		})

	});


	/*
		对于所有的关系型数据库，做前端的分页相关操作的基础组件
		就是pageNo和pageSize
		pageNo：页面
		pageSize：每页展示的记录数

		pageList方法：就是发出ajax请求到后台，从后台取出最新的市场活动信息列表的数据
			通过响应回来的数据，局部刷新市场活动列表。

		我们都在哪些情况下，需要调用pageList方法（什么情况下需要刷新一下市场活动列表）
		1) 点击左侧菜单栏中的“市场活动”超链接，需要刷新市场活动列表，调用pageList方法
		2) 添加、修改、删除后，需要刷新市场活动列表，调用pageList方法。
		3) 点击查询按钮的时候，需要刷新市场活动列表，调用pageList方法。
		4) 点击分页组件的时候，调用pageList方法。

		以上为pageList方法指定了六个入口，也就是说，在以上6个操作执行完毕后，我们必须要调用pageList方法，刷新市场活动列表。

	 */
	function pageList(pageNo, pageSize){

		// 将全选的复选框的勾儿干掉
		$("#qx").prop("checked", false);

		// alert("市场活动列表");

		// 查询前，将隐藏域中保存的信息取出来，重新赋予到搜索框中
		$("#search-name").val($.trim($("#hidden-name").val()));
		$("#search-owner").val($.trim($("#hidden-owner").val()));
		$("#search-startDate").val($.trim($("#hidden-startDate").val()));
		$("#search-endDate").val($.trim($("#hidden-endDate").val()));

		$.ajax({
			url:"workbench/activity/pageList.do",
			data:{
				"pageNo":pageNo,
				"pageSize":pageSize,
				"name":$.trim($("#search-name").val()),
				"owner":$.trim($("#search-owner").val()),
				"startDate":$.trim($("#search-starDate").val()),
				"endDate":$.trim($("#search-endDate").val())
			},
			dataType:"json",
			type:"get",
			success:function(data){

				/*
					data
						我们需要的，市场活动信息列表
						[{市场活动1},{},{}...] List<Activity> aList
						一会分页插件需要的：查询出来的总记录数
						{"total":100}  int total
						{"total":100,"dataList":[
							{市场活动1},{},{}...
						]}
				 */


				var html = "";
				// 每一个activity就是每一个市场活动对象
				$.each(data.dataList, function(index, a){
					html += '<tr class="active">';
					html += '<td><input type="checkbox" name="xz" value="'+a.id+'"/></td>';
					html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.jsp\';">'+a.name+'</a></td>';
					html += '<td>'+a.owner+'</td>';
					html += '<td>'+a.startDate+'</td>';
					html += '<td>'+a.endDate+'</td>';
					html += '</tr>';
				});

				$("#activityBody").html(html);

				// 计算总页数
				var totalPages = data.total % pageSize == 0 ? data.total/pageSize: parseInt(data.total/pageSize) + 1;

				// 数据处理完毕之后，结合分页插件，对前端展现分页相关的信息
				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize,	// 每页显示的记录条数
					maxRowsPerPage: 20,	// 每页显示的记录条数
					totalPages: totalPages,	// 总页数
					totalRows: data.total,	// 总记录条数

					visiblePageLinks: 3,	// 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					// 该回调函数是在，点击分页组件的时候触发的
					onChangePage: function(event, data){
						pageList(data.currentPage, data.rowsPerPage);
					}
				});


			},
		})
	}

</script>
</head>
<body>
<!--由隐藏域保存4个搜索框中的内容-->
<input type="hidden" id="hidden-name" />
<input type="hidden" id="hidden-owner" />
<input type="hidden" id="hidden-startDate" />
<input type="hidden" id="hidden-endDate" />
	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">

					<form  id="activityAddForm" class="form-horizontal" role="form">

						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">
<%--								  <option>zhangsan</option>--%>
<%--								  <option>lisi</option>--%>
<%--								  <option>wangwu</option>--%>
									
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate" readonly>
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">

					<!--
						data-dismiss="modal"
							表示关闭模态窗口
					-->
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">
								  <option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-startTime" value="2020-10-10">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-endTime" value="2020-10-20">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="5,000">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-starDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="search-endDate">
				    </div>
				  </div>
				  
				  <button id="searchBtn" type="button" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
					<!--

						点击创建按钮，观察两个属性和属性值

						data-toggle="modal"：
							表示触发该按钮，将要打开一个模态窗口

						data-target="#createActivityModal"：
							表示要打开哪个模态窗口

						现在我们是以属性和属性值的方式写在了button元素中用来打开模态窗口。
						但是这样做是有问题的：
							问题在于没有办法对按钮的功能进行扩展（点击该按钮，仅仅只是打开了模态窗口）
					-->
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" data-toggle="modal" data-target="#editActivityModal"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx" /></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
<%--						<tr class="active">--%>
<%--							<td><input type="checkbox" /></td>--%>
<%--							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>--%>
<%--                            <td>zhangsan</td>--%>
<%--							<td>2020-10-10</td>--%>
<%--							<td>2020-10-20</td>--%>
<%--						</tr>--%>
<%--                        <tr class="active">--%>
<%--                            <td><input type="checkbox" /></td>--%>
<%--                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>--%>
<%--                            <td>zhangsan</td>--%>
<%--                            <td>2020-10-10</td>--%>
<%--                            <td>2020-10-20</td>--%>
<%--                        </tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage">

				</div>
			</div>
			
		</div>
		
	</div>
</body>
</html>