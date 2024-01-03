package com.tathvatech.common.wrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public class PersistWrapperImpl implements PersistWrapper {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public <T> List<T> readList(final Class<T> objectClass, final String sql, final Object...parameters)
	{
		RowMapper<T> rowMapper = BeanPropertyRowMapper.newInstance(objectClass);		
		return jdbcTemplate.query(sql, rowMapper, parameters);
	}
	
	
	@Override
    public long createEntity(Object inObject) throws Exception
    {
		String tableName = inObject.getClass().getAnnotation(Table.class).name();
		
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
		          .withTableName(tableName)
		          .usingGeneratedKeyColumns("pk");
		
		return simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(inObject)).longValue();    
    }
	
	@Override
	public void update(Object inObject)
	{
		String tableName = inObject.getClass().getAnnotation(Table.class).name();

		List<Object> values = new ArrayList<Object>();
		List<Integer> argTypes = new ArrayList<Integer>();
		
		String keyColumn = "pk";

		BeanPropertySqlParameterSource pSource = new BeanPropertySqlParameterSource(inObject);
		String[] paramNames = pSource.getParameterNames();
		
		StringBuilder sb = new StringBuilder();
		sb.append("update ").append(tableName).append(" set ");
		
		String coma = "";
		for (int i = 0; i < paramNames.length; i++) {
			if("class".equals(paramNames[i]))
				continue;
			if(keyColumn.equals(paramNames[i]))
				continue;
			
			Object paramValue = pSource.getValue(paramNames[i]);
			if(paramValue != null)
			{
				sb.append(coma);
				sb.append(paramNames[i] + "= ? ");
				coma = ", ";
				values.add(pSource.getValue(paramNames[i]));
				argTypes.add(pSource.getSqlType(paramNames[i]));
			}
			else
			{
				sb.append(coma);
				sb.append(paramNames[i] + "= null ");
				coma = ", ";
			}
		}
		sb.append(" where ").append(keyColumn).append(" = ? ");
		values.add(pSource.getValue(keyColumn));
		argTypes.add(pSource.getSqlType(keyColumn));
		System.out.println(sb.toString());
		
		jdbcTemplate.update(sb.toString(), values.toArray(), argTypes.toArray(new Integer[] {}));
		
	}
}
