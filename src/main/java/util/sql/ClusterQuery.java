/**@author:idevcod@163.com
 * @date:2015年12月27日下午9:16:12
 * @description:<TODO>
 */
package util.sql;

import java.util.ArrayList;
import java.util.List;

public class ClusterQuery {
	private String[] columns;
	private String[] conditions;
	private String order;
	private String whereClause;
	
	public ClusterQuery select(String... coloumns){
		this.columns = coloumns;
		return this;
	}
	
	public ClusterQuery where(String whereClause,String... conditios){
		this.whereClause  = whereClause;
		this.conditions = conditios;
		return this;
	}
	
	public ClusterQuery order(String order){
		this.order = order;
		return this;
	}
	
	public <T> List<T> find(Class<T> tableModel){
		List<T> dataList = new ArrayList<T>();
		//TODO
		return dataList;
	}
}
