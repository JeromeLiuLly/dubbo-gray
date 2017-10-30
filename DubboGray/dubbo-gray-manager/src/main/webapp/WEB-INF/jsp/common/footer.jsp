<%--
  Created by IntelliJ IDEA.
  User: bieber
  Date: 2015/4/26
  Time: 18:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

	<!-- 模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
					&times;
				</button>
				<h4 class="modal-title" id="myModalLabel">
					编辑灰度服务
				</h4>
			</div>
			<div class="modal-body">
			    <input id="id"  type="hidden" />
				灰度名称：<input id="strategyName" class="form-control" type="text"/>
				服务提供者：<input id="serviceId" class="form-control" type="text"/>
				服务地址：<input id="serviceName" class="form-control" type="text"/>
				灰度标签：<input id="serviceTag" class="form-control" type="text"/>
				灰度值域: <input id="strategyValue" class="form-control" type="text"/>
				权重: <input id="weight" class="form-control" type="number"/>
				服务版本:<input id="version" class="form-control" type="text"/>
				服务状态:<select id="status" class="form-control">
  							<option value="1">生效</option>
  							<option value="0">关闭</option>
					</select>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭
				</button>
				<button type="button" id="submitOP" class="btn btn-primary">
					提交更改
				</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal -->
</div>

<!-- 模态框（Modal） -->
<div class="modal fade" id="myUserModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
					&times;
				</button>
				<h4 class="modal-title" id="myUserModalLabel">
					编辑用户灰度策略
				</h4>
			</div>
			<div class="modal-body">
			    <input id="id"  type="hidden" />
				用户名：<input id="userName" class="form-control" type="text"/>
				灰度标签：<input id="serviceTagUser" class="form-control" type="text"/>
				灰度值域：<input id="serviceValueUser" class="form-control" type="text"/>
				灰度策略：<input id="strategy" class="form-control" type="text"/>
				权重: <input id="weightUser" class="form-control" type="number"/>
				服务状态:<select id="statusUser" class="form-control">
  							<option value="1">生效</option>
  							<option value="0">关闭</option>
					</select>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭
				</button>
				<button type="button" id="submitUserOP" class="btn btn-primary">
					提交更改
				</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal -->
</div>

</body>
</html>

