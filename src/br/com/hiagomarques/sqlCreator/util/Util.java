package br.com.hiagomarques.sqlCreator.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import br.com.hiagomarques.sqlCreator.anotations.Id;

public class Util {

	public static List<String> getNamesFromSql(String sql) {
		List<String> arr = new ArrayList<>();
		if (sql.toLowerCase().startsWith("insert")) {
			arr = Arrays.asList(
					sql.substring(sql.indexOf("("), sql.indexOf(")")).replace("(", "").replace(")", "").split(","));
		} else {
			int start = 0;
			do {
				start = sql.indexOf(" = ?", start + 1);
				String palavra = "";
				for (int i = start; i > 0; i--) {
					char letra = sql.charAt(i - 1);
					if (" ".equals(String.valueOf(letra))) {
						break;
					}
					palavra = letra + palavra;
				}
				if (start != -1) {
					arr.add(palavra);
				}

			} while (sql.indexOf(" = ?", start + 1) != -1);
		}
		arr = arr.stream().map(String::trim).collect(Collectors.toList());
		return arr;
	}

	public static List<String> getAllNamesFromClass(Class<? extends Object> reflected) {
		return Stream.of(reflected.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> resultSetFiler(T obj, ResultSet result, List<String> fields) throws SQLException,
			InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<T> objs = new ArrayList<T>();
		while (result.next()) {
			Class<? extends Object> molde = obj.getClass();
			Object emptyObj = molde.getConstructor().newInstance();
			fields.forEach(field -> {
				try {
					Field fil = molde.getDeclaredField(field);
					fil.setAccessible(true);
					fil.set(emptyObj, result.getObject(field));
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			objs.add((T) emptyObj);

		}
		return objs;
	}
	public static <T> T copyFields(T entity, T newEntity, Class<?> clazz) throws IllegalAccessException {
	    List<Field> fields = new ArrayList<>();
	    for (Field field : clazz.getDeclaredFields()) {
	        fields.add(field);
	    }
	    for (Field field : fields) {
	        field.setAccessible(true);
	        if ( field.get(entity) != null ) {
	        	field.set(newEntity, field.get(entity));
	        }
	    }
	    return newEntity;
	}
	public static<T> Field idField(T obj) throws Exception {
		Field field = Stream.of(obj.getClass().getDeclaredFields())
				.filter(fil -> fil.getAnnotation(Id.class) != null).findFirst()
				.orElseThrow(() -> new RuntimeException("Id não encontrado"));
		field.setAccessible(true);
	
	return field;
	
}
}
