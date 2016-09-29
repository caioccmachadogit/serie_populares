package android.test.seriespopularesapp.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.test.seriespopularesapp.model.Favorito;
import android.test.seriespopularesapp.util.db.GerenciadorDB;
import android.test.seriespopularesapp.util.db.convert.DatabaseConverter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by re032629 on 28/09/2016.
 */

public class FavoritoRepository {

    private Context context;

    public FavoritoRepository(Context context) {
        this.context = context;
    }

    private final String NomeTabela = "favoritos";
	public static String createTable = "CREATE TABLE 'favoritos' ("+
			"_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
			  "idSerie varchar(50) DEFAULT NULL);";

    private GerenciadorDB gerDB = new GerenciadorDB();

    public long insert(Favorito favorito) throws Exception{
        long resultadoInsercao = 0;
		try{
			if(favorito != null){
                ContentValues contentValues = DatabaseConverter.convertObjectToContentValue(favorito);
                gerDB.checkOpeningDataBase(context, "write");
                resultadoInsercao = GerenciadorDB.db.insert(NomeTabela, null, contentValues);
				GerenciadorDB.db = gerDB.closeDataBase(context);
			}
		}
		catch(Exception ex){
			Log.e("ERROR DE insert",NomeTabela+"=="+ex.getMessage());
			resultadoInsercao = -1;
			throw ex;
		}
		return resultadoInsercao;
    }

    public Favorito readByIdSerie(String idSerie) throws Exception{
		Cursor retorno;
		String dump = null;
		List<Favorito> lst = new ArrayList<>();
		try{
			gerDB.checkOpeningDataBase(context, "read");
			retorno = GerenciadorDB.db.rawQuery("SELECT * FROM "+NomeTabela+" WHERE idSerie ='"+idSerie+"'", null);
			dump = DatabaseUtils.dumpCursorToString(retorno);
			GerenciadorDB.db = gerDB.closeDataBase(context);
			lst = DatabaseConverter.convertCursorToObject(retorno, Favorito.class);
		}
		catch(Exception ex){
            Log.e("ERROR DE readByIdSerie",NomeTabela+"=="+ex.getMessage());
			throw ex;
		}
		Log.v("Valores Cursor", dump);
		return (lst.size() > 0 ? lst.get(0) : null);
	}

	public int deleteByIdSerie(String idSerie) throws Exception{
		int retorno = 0;
		String[] whereArqs = new String[]{idSerie};
		try{
			gerDB.checkOpeningDataBase(context, "write");
			retorno = GerenciadorDB.db.delete(NomeTabela,"idSerie=?",whereArqs);
			GerenciadorDB.db = gerDB.closeDataBase(context);
		}
		catch(Exception ex){
			//LogErrorBLL.LogError("", "ERROR DE PERSISTENCIA NO LIST "+NomeTabela ,context);
			retorno = -1;
			throw ex;
		}
		return retorno;
	}


}
