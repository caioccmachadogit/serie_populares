package android.test.seriespopularesapp.util.db.convert;

import android.content.ContentValues;
import android.database.Cursor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by re032629 on 21/07/2015.
 */
public class DatabaseConverter {

    private static Map<Class, List<BeanInfo>> beansInfoMapper = Collections.synchronizedMap(new HashMap<Class, List<BeanInfo>>());

    public static ContentValues convertObjectToContentValue(Object object) {

        ContentValues contentValues = new ContentValues();

        if (object == null || object.getClass().getAnnotation(DatabaseBeans.class) == null) {
            return contentValues;
        }

        BeanInfo beansInfo;

        if (!beansInfoMapper.containsKey(object.getClass())) {
            initObjectInfo(beansInfoMapper, object.getClass());
        }

        List<BeanInfo> beansInfoList = beansInfoMapper.get(object.getClass());

        String tempColumn;
        Field tempField;
        Object tempValueObj;
        Class tempFieldType;

        for (int idx = 0, size = beansInfoList.size(); idx < size; ++idx) {
            beansInfo = beansInfoList.get(idx);

            if (beansInfo != null && !beansInfo.isReadOnly()) {
                tempColumn = beansInfo.getColumn();

                tempField = beansInfo.getField();
                tempField.setAccessible(true);
                try {
                    tempValueObj = tempField.get(object);
                    tempFieldType = beansInfo.getType();

                    putValue(contentValues, tempColumn, tempValueObj, tempFieldType);

                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                tempField.setAccessible(false);
            }
        }

        return contentValues;
    }

    protected static void initObjectInfo(Map<Class, List<BeanInfo>> beansInfos, Class clazz) {

        ArrayList<BeanInfo> beansInfosList = new ArrayList<BeanInfo>();
        beansInfos.put(clazz, beansInfosList);

        Field tempField;

        Column fieldAnnotation;
        String tempColumn;
        Class tempFieldType;

        Field[] fields = clazz.getDeclaredFields();

        for (int idx = 0, size = fields.length; idx < size; ++idx) {
            tempField = fields[idx];
            fieldAnnotation = tempField.getAnnotation(Column.class);

            if (fieldAnnotation != null) {
                tempColumn = fieldAnnotation.column();

                if (tempColumn == null || tempColumn.length() == 0) {
                    tempColumn = tempField.getName();
                }

                tempField.setAccessible(true);
                try {
                    tempFieldType = tempField.getType();

                    beansInfosList.add(new BeanInfo(tempField, tempColumn, tempFieldType, tempField.isAnnotationPresent(Id.class)));

                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                tempField.setAccessible(false);
            }
        }

    }

    public static <T> List<T> convertCursorToObject(Cursor cursor, Class<T> clazz) {

        if (!beansInfoMapper.containsKey(clazz)) {
            initObjectInfo(beansInfoMapper, clazz);
        }

        List<T> itemList = new ArrayList<T>();
        if (cursor == null || cursor.getCount() <= 0) {
            return itemList;
        }

        List<BeanInfo> beansInfoList = beansInfoMapper.get(clazz);
        if (beansInfoList == null || beansInfoList.size() == 0) {
            return itemList;
        }

        // Column : ColumnIdx
        Map<String, Integer> values = null;

        Field tempField;
        BeanInfo tempBeans;
        T obj;
        while (cursor.moveToNext()) {
            obj = null;
            try {
                Constructor<T> declaredConstructor = clazz.getDeclaredConstructor();
                obj = declaredConstructor.newInstance(new Object[]{});
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            if (obj == null) {
                continue;
            }
            if (values == null) {
                values = new HashMap<String, Integer>();
                for (int idx = 0, size = cursor.getColumnCount(); idx < size; ++idx) {
                    values.put(cursor.getColumnName(idx), idx);
                }

            }

            for (int idx = 0, size = beansInfoList.size(); idx < size; ++idx) {
                tempBeans = beansInfoList.get(idx);
                tempField = tempBeans.getField();
                tempField.setAccessible(true);
                String column = tempBeans.getColumn();
                int columnIndex = values.get(column);
                if (columnIndex != -1) {
                    putObject(cursor, tempField, obj, columnIndex);
                }
                tempField.setAccessible(false);
            }

            itemList.add(obj);
        }

        return itemList;
    }

    protected static void putObject(Cursor cursor, Field tempField, Object obj, int columnIndex) {
        try {
            Class<?> fieldType = tempField.getType();
            if (fieldType == int.class || fieldType == Integer.class) {
                tempField.set(obj, cursor.getInt(columnIndex));
            } else if (fieldType == String.class) {
                tempField.set(obj, cursor.getString(columnIndex));
            } else if (fieldType == long.class || fieldType == Long.class) {
                tempField.set(obj, cursor.getLong(columnIndex));
            } else if (fieldType == boolean.class || fieldType == Boolean.class) {
                tempField.set(obj, cursor.getString(columnIndex).equals(Boolean.TRUE));
            } else if (fieldType == double.class || fieldType == Double.class) {
                tempField.set(obj, cursor.getDouble(columnIndex));
            } else if (fieldType == float.class || fieldType == Float.class) {
                tempField.set(obj, cursor.getFloat(columnIndex));
            } else if (fieldType == short.class || fieldType == Short.class) {
                tempField.set(obj, cursor.getShort(columnIndex));
            } else if (fieldType == byte.class || fieldType == Byte.class) {
                // tempField.set(obj, cursor.getInt(columnIndex));
            }

            // FIXME Add for byte[] column
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }

    }

    protected static void putValue(ContentValues contentValues, String tempColumn, Object tempValueObj,
                                   Class tempFieldType) {

        if (tempFieldType == int.class || tempFieldType == Integer.class) {
            contentValues.put(tempColumn, (Integer) tempValueObj);
        } else if (tempFieldType == String.class) {
            contentValues.put(tempColumn, (String) tempValueObj);
        } else if (tempFieldType == long.class || tempFieldType == Long.class) {
            contentValues.put(tempColumn, (Long) tempValueObj);
        } else if (tempFieldType == boolean.class || tempFieldType == Boolean.class) {
            contentValues.put(tempColumn, (Boolean) tempValueObj);
        } else if (tempFieldType == double.class || tempFieldType == Double.class) {
            contentValues.put(tempColumn, (Double) tempValueObj);
        } else if (tempFieldType == float.class || tempFieldType == Float.class) {
            contentValues.put(tempColumn, (Float) tempValueObj);
        } else if (tempFieldType == short.class || tempFieldType == Short.class) {
            contentValues.put(tempColumn, (Short) tempValueObj);
        } else if (tempFieldType == byte.class || tempFieldType == Byte.class) {
            contentValues.put(tempColumn, (Byte) tempValueObj);
        }
        // FIXME Add for byte[] column
    }

}
