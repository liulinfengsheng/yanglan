package com.example.server;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class KNN {
	private DBRead dbread;
	private List<FingerPrinting> finger;
	private ResultSet rs;
	
	
	public KNN(DBRead dbread1)
	{
		this.dbread=dbread1;
        finger=new ArrayList<FingerPrinting>();
		rs=dbread.DatabaseRead();
		try {
			while(rs.next())
			{
//				rs.get
				
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	}

}
