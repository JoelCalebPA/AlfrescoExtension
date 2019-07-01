<html>
  <head>
    <title>Audit</title>
	<style>
		table {
		  border-spacing: 1;
		  border-collapse: collapse;
		  background: white;
		  border-radius: 6px;
		  overflow: hidden;
		  max-width: 800px;
		  width: 100%;
		  margin: 0 auto;
		  position: relative;
		}
		table * {
		  position: relative;
		}
		table td, table th {
		  padding-left: 8px;
		}
		table thead tr {
		  height: 60px;
		  background: #FFED86;
		  font-size: 16px;
		}
		table tbody tr {
		  height: 48px;
		  border-bottom: 1px solid #E3F1D5;
		}
		table tbody tr:last-child {
		  border: 0;
		}
		table td, table th {
		  text-align: left;
		}
		table td.l, table th.l {
		  text-align: right;
		}
		table td.c, table th.c {
		  text-align: center;
		}
		table td.r, table th.r {
		  text-align: center;
		}

		body {
		  background: #9BC86A;
		  font: 400 14px 'Calibri','Arial';
		  padding: 20px;
		}

		blockquote {
		  color: white;
		  text-align: center;
		}
	</style>
  </head>
  <body>
  <form id="form-Reporte" name="form-Reporte" method="get" accept-charset="utf-8">
  <div>
		  <div>
		    <table>
	    		<tr>
		    		<td><label>Usuario</label></td>
		    		<td colspan="3"><input id="user" name="user" type="Text" value='${(args.user)!""}' style="width: 70%" /></td>
	    		</tr>
				<tr>
		    		<td><label>Acción</label></td>
		    		<td colspan="3"><input id="action" name="action" type="Text" value='${(args.action)!""}' style="width: 70%" /></td>
	    		</tr>
	    		<tr>
		    		<td><label>Documento</label></td>
		    		<td colspan="3"><input id="document" name="document" type="Text" value='${(args.document)!""}' style="width: 70%" /></td>
	    		</tr>
	    		<tr>
		    		<td><label>Fecha Inicial</label></td>
		    		<td><input id="datepickerIni" name="dateIni" readonly="readonly" type="text" value='${(args.dateIni)!""}' /></td>
		    		<td><label>Fecha Final</label></td>
		    		<td><input id="datepickerFin" name="dateFin" readonly="readonly" type="text" value='${(args.dateFin)!""}' /></td>
	    		</tr>
				<tr>
					<td colspan="4"> <input type="submit" value='Buscar' /></td>
				</tr>
		    </table>
		    
		  </div>
		  <div>
  </form>
  <div>
	<table id="documentos" border="1"  >
		<thead>
			<tr >
				<th>Usuario</th>
				<th>Acción</th>
				<th>Documento</th>
				<th>Fecha</th>
			</tr>
		</thead>
		<tbody>
			<#if result??>
			<#list result as doc>
			   <tr >
				   <td>${doc.user}</td>
				   <td>${doc.action}</td>
					<td>${doc.document}</td>
				   <td>${doc.date?string["dd-MM-yyyy, HH:mm:ss"]}</td>
			   </tr>
			</#list>
			</#if>
		</tbody>
	</table>
	</div>
  </body>
</html>
    