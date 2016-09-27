package android.test.seriespopularesapp.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by re032629 on 25/09/2016.
 */

public class ListOfJson<T> implements ParameterizedType
{
  private Class<?> wrapped;

  public ListOfJson(Class<T> wrapper)
  {
    this.wrapped = wrapper;
  }

  @Override
  public Type[] getActualTypeArguments()
  {
      return new Type[] { wrapped };
  }

  @Override
  public Type getRawType()
  {
    return List.class;
  }

  @Override
  public Type getOwnerType()
  {
    return null;
  }
}
