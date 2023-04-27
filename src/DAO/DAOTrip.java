package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import BUS.JDBCUtil;
import Models.Route;
import Models.Trip;
import Models.myDate;

public class DAOTrip implements DAOInterface<Trip> {

	public static DAOTrip getInstance() {
		return new DAOTrip();
	}

	@Override
	public int insert(Trip t) throws SQLException, ClassNotFoundException {

		Connection con = JDBCUtil.getConnection();

		String command = "INSERT INTO TripInDay (TimeStart,TimeEnd,RouteID,DateStart,DateEnd) VALUES ( ?, ?, ?, ?, ?)";
		PreparedStatement psm = con.prepareStatement(command);

		psm.setTime(1, Time.valueOf(t.getTimeStart()));
		psm.setTime(2, Time.valueOf(t.getTimeEnd()));
		psm.setString(3, t.getRouteID());
		psm.setDate(4, Date.valueOf(t.getDateStart()));
		psm.setDate(5, Date.valueOf(t.getDateEnd()));

		int executedRow = psm.executeUpdate();

		psm.close();

		JDBCUtil.closeConnection(con);
		return executedRow;
	}

	@Override
	public int delete(Trip t) throws SQLException, ClassNotFoundException {

		Connection con = JDBCUtil.getConnection();

		String command = "DELETE FROM TripInDay WHERE TripID = ?";
		PreparedStatement psm = con.prepareStatement(command);

		psm.setInt(1, t.getTripID());

		int executedRow = psm.executeUpdate();

		psm.close();

		JDBCUtil.closeConnection(con);
		return executedRow;
	}

	@Override
	public int update(Trip t) throws SQLException, ClassNotFoundException {

		Connection con = JDBCUtil.getConnection();

		String command = "UPDATE TripInDay SET TimeStart = ?, TimeEnd = ?, RouteID = ?, DateStart = ?, DateEnd = ? WHERE TripID = ?";
		PreparedStatement psm = con.prepareStatement(command);

		psm.setInt(6, t.getTripID());
		psm.setTime(1, Time.valueOf(t.getTimeStart()));
		psm.setTime(2, Time.valueOf(t.getTimeEnd()));
		psm.setString(3, t.getRouteID());
		psm.setDate(4, Date.valueOf(t.getDateStart()));
		psm.setDate(5, Date.valueOf(t.getDateEnd()));

		int executedRow = psm.executeUpdate();

		psm.close();

		JDBCUtil.closeConnection(con);
		return executedRow;
	}

	@Override
	public ArrayList<Trip> selectAll() throws SQLException, ClassNotFoundException {

		Connection Conn = JDBCUtil.getConnection();
		List<Trip> list = new ArrayList<>();

		// Bước 3 : Thực hiện câu lệnh truy vấn
		Statement stmt = Conn.createStatement();
		String sqlCommand = "SELECT * FROM TripInDay";
		ResultSet rs = stmt.executeQuery(sqlCommand);

		// Bước 4 : Xem thong tin cua bang
		while (rs.next()) {
			int TripID = rs.getInt("TripID");
			LocalTime TimeStart = rs.getTime("TimeStart").toLocalTime();
			LocalTime TimeEnd = rs.getTime("TimeEnd").toLocalTime();
			String Route = rs.getString("RouteID");
			LocalDate DateStart = rs.getDate("DateEnd").toLocalDate();
			LocalDate DateEnd = rs.getDate("DateStart").toLocalDate();

			Trip temp = new Trip(TripID, Route, TimeStart, TimeEnd, DateStart, DateEnd);
			list.add(temp);
		}

		rs.close();
		stmt.close();

		JDBCUtil.closeConnection(Conn);
		return (ArrayList<Trip>) list;
	}

	@Override
	public Trip selectByID(String t) throws SQLException, ClassNotFoundException {
		Connection Conn = JDBCUtil.getConnection();

		String SqlCommand = "SELECT * FROM TripInDay WHERE TripID = ?";
		PreparedStatement psm = Conn.prepareStatement(SqlCommand);

		psm.setInt(1, Integer.parseInt(t));

		ResultSet rs = psm.executeQuery();

		Trip result = new Trip(0, "", LocalTime.of(0, 0), LocalTime.of(0, 0), LocalDate.of(2023, 4, 20),
				LocalDate.of(2023, 4, 20));

		if (rs.next()) {

			result = new Trip(rs.getInt("TripID"), rs.getString("RouteID"), rs.getTime("TimeStart").toLocalTime(),
					rs.getTime("TimeEnd").toLocalTime(), rs.getDate("DateStart").toLocalDate(), rs.getDate("DateEnd").toLocalDate());
		}

		rs.close();
		psm.close();
		JDBCUtil.closeConnection(Conn);
		return result;
	}

	@Override
	public ArrayList<Trip> selectByCondition(String condition) throws SQLException, ClassNotFoundException {
		Connection Conn = JDBCUtil.getConnection();

		ArrayList<Trip> list = new ArrayList<>();

		Statement stmt = Conn.createStatement();
		String command = "SELECT * FROM TripInDay WHERE " + condition;
		ResultSet rs = stmt.executeQuery(command);

		while (rs.next()) {
			LocalDate DateStart = rs.getDate("DateStart").toLocalDate();
			LocalDate DateEnd = rs.getDate("DateEnd").toLocalDate();

			list.add(new Trip(rs.getInt("TripID"), rs.getString("RouteID"), rs.getTime("TimeStart").toLocalTime(),
					rs.getTime("TimeEnd").toLocalTime(), DateStart, DateEnd));
		}

		rs.close();
		stmt.close();
		JDBCUtil.closeConnection(Conn);
		return list;
	}

	public static void InsertTrip() throws ClassNotFoundException, SQLException {
		List<Route> list = DAORoute.getInstance().selectAll();
		Connection con = JDBCUtil.getConnection();
		Statement st = con.createStatement();
		String command = "Delete from TripInDay";
		st.executeUpdate(command);
		
		LocalTime MorningTime = LocalTime.of(7,0,0);
		LocalTime AfternoonTime = LocalTime.of(13,0,0);
		
		LocalTime EndTimeMorning;
		LocalTime EndTimeAfternoon;
		
		int EndHourMo = 0, EndHourAf = 0;
		
		for (int i = 0; i < list.size(); i++) 

			for (int j = 4; j <= 4; j++) 
				
				for (int h = 1; h < myDate.getNumberDateOfMonth(j, 2023); h++) {
					
					Route r = DAORoute.getInstance().selectByID(list.get(i).getRouteID());
					
					// j là tháng còn h là ngày
					
					EndHourMo = MorningTime.getHour() + r.getDuration().getHours();
					EndHourAf = AfternoonTime.getHour() + r.getDuration().getHours();
					
					if (EndHourMo < 24) {
						Trip temp = new Trip(0, list.get(i).getRouteID(), new Time(7, 0, 0).toLocalTime(),
								new Time(EndHourMo, 0, 0).toLocalTime(), LocalDate.of(2023, j, h),
								LocalDate.of(2023, j, h));
						DAOTrip.getInstance().insert(temp);
					} else {
						Trip temp = new Trip(0, list.get(i).getRouteID(), new Time(7, 0, 0).toLocalTime(),
								new Time(EndHourMo -24 , 0, 0).toLocalTime(),
								LocalDate.of(2023, j, h), LocalDate.of(2023, j, h + 1));
						DAOTrip.getInstance().insert(temp);
					}
					if (EndHourAf < 24) {
						Trip temp2 = new Trip(0, list.get(i).getRouteID(), new Time(13, 0, 0).toLocalTime(),
								new Time(EndHourAf, 0, 0).toLocalTime(),
								LocalDate.of(2023, j, h), LocalDate.of(2023, j, h));
						DAOTrip.getInstance().insert(temp2);
					} else {
						Trip temp2 = new Trip(0, list.get(i).getRouteID(), new Time(13, 0, 0).toLocalTime(),
								new Time(EndHourAf -24, 0, 0).toLocalTime(), LocalDate.of(2023, j, h),
								LocalDate.of(2023, j, h+1));
						DAOTrip.getInstance().insert(temp2);
					}
		}
		JDBCUtil.closeConnection(con);
	}

	public int getNextID() throws SQLException
	{
		Connection Conn = JDBCUtil.getConnection(); 
		
		Statement stmt = Conn.createStatement();
		String sqlCommand = "SELECT TripID FROM TripInDay";
		ResultSet rs = stmt.executeQuery(sqlCommand); 

		int result = 0;
				
		while(rs.next())
		{
			result = rs.getInt("TripID");
		}
		
		rs.close();
		stmt.close();
		JDBCUtil.closeConnection(Conn);

		return result + 1;
	}

	public String getNameCityStart(int TripID) throws SQLException
	{
		Connection Conn = JDBCUtil.getConnection();

		String SqlCommand = "Select CityName From City AS C\r\n"
				+ "JOIN RouteWay AS RW ON RW.CityIDStart = C.CityID\r\n"
				+ "JOIN TripInDay AS TID ON TID.RouteID = RW.RouteID\r\n"
				+ "WHERE TID.TripID = ?";
		PreparedStatement psm = Conn.prepareStatement(SqlCommand);

		psm.setInt(1, TripID);

		ResultSet rs = psm.executeQuery();

		String result = "";

		if (rs.next()) 
		{
			result = rs.getString("CityName");
		}

		rs.close();
		psm.close();
		JDBCUtil.closeConnection(Conn);
		return result;
	}
	
	public String getNameCityEnd(int TripID) throws SQLException
	{
		Connection Conn = JDBCUtil.getConnection();

		String SqlCommand = "Select CityName From City AS C\r\n"
				+ "JOIN RouteWay AS RW ON RW.CityIDEnd = C.CityID\r\n"
				+ "JOIN TripInDay AS TID ON TID.RouteID = RW.RouteID\r\n"
				+ "WHERE TID.TripID = ?";
		PreparedStatement psm = Conn.prepareStatement(SqlCommand);

		psm.setInt(1, TripID);

		ResultSet rs = psm.executeQuery();

		String result = "";

		if (rs.next()) 
		{
			result = rs.getString("CityName");
		}

		rs.close();
		psm.close();
		JDBCUtil.closeConnection(Conn);
		return result;
	}

	public int getDistance(int TripID) throws SQLException
	{
		Connection Conn = JDBCUtil.getConnection();

		String SqlCommand = "Select Distance From RouteWay AS RW\r\n"
				+ "JOIN TripInDay AS TID ON TID.RouteID = RW.RouteID\r\n"
				+ "WHERE TID.TripID = ?";
		PreparedStatement psm = Conn.prepareStatement(SqlCommand);

		psm.setInt(1, TripID);

		ResultSet rs = psm.executeQuery();

		int result = 0;

		if (rs.next()) 
		{
			result = rs.getInt("Distance");
		}

		rs.close();
		psm.close();
		JDBCUtil.closeConnection(Conn);
		return result;
	}

	public int getBasePrice(int TripID) throws SQLException
	{
		Connection Conn = JDBCUtil.getConnection();

		String SqlCommand = "Select BasePrice From RouteWay AS RW\r\n"
				+ "JOIN TripInDay AS TID ON TID.RouteID = RW.RouteID\r\n"
				+ "WHERE TID.TripID = ?";
		PreparedStatement psm = Conn.prepareStatement(SqlCommand);

		psm.setInt(1, TripID);

		ResultSet rs = psm.executeQuery();

		int result = 0;

		if (rs.next()) 
		{
			result = rs.getInt("BasePrice");
		}

		rs.close();
		psm.close();
		JDBCUtil.closeConnection(Conn);
		return result;
	}

	public int getCountBusySeat(int TripID) throws SQLException
	{
		int result = 0;
		Connection Conn = JDBCUtil.getConnection(); 
		
		String SqlCommand = "SELECT COUNT(*) FROM Ticket WHERE TripID = ?";
		PreparedStatement psm = Conn.prepareStatement(SqlCommand);
		
		psm.setInt(1, TripID);
		
		ResultSet rs = psm.executeQuery();

		if(rs.next())
		{
			result = rs.getInt(1);
		}
		
		rs.close();
		psm.close();
		JDBCUtil.closeConnection(Conn);
		return result;
	}

	public int getBusSeatNumber(int TripID) throws SQLException
	{
		Connection Conn = JDBCUtil.getConnection();

		String SqlCommand = "Select B.NumberOfSeat From RouteWay AS RW\r\n"
				+ "JOIN TripInDay AS TID ON TID.RouteID = RW.RouteID\r\n"
				+ "JOIN Bus AS B ON B.BusID = RW.BusID\r\n"
				+ "WHERE TID.TripID = ?";
		PreparedStatement psm = Conn.prepareStatement(SqlCommand);

		psm.setInt(1, TripID);

		ResultSet rs = psm.executeQuery();

		int result = 0;

		if (rs.next()) 
		{
			result = rs.getInt("NumberOfSeat");
		}

		rs.close();
		psm.close();
		JDBCUtil.closeConnection(Conn);
		return result;
	}

	public List<Trip> getListByRouteAndDate(String CityIDStart, String CityIDEnd, LocalDate date) throws SQLException
	{
		Connection Conn = JDBCUtil.getConnection();
		
		String SqlCommand = "Select TID.* from TripInDay AS TID\r\n"
				+ "JOIN RouteWay AS RW ON RW.RouteID = TID.RouteID\r\n"
				+ "WHERE RW.CityIDStart = ?\r\n"
				+ "AND RW.CityIDEnd = ?\r\n"
				+ "AND TID.DateStart = ?";
		PreparedStatement psm = Conn.prepareStatement(SqlCommand);

		psm.setString(1, CityIDStart);
		psm.setString(2, CityIDEnd);
		psm.setDate(3, Date.valueOf(date));

		ResultSet rs = psm.executeQuery();

		List<Trip> list = new ArrayList<>();
		
		while (rs.next()) 
		{
			LocalDate DateStart = rs.getDate("DateStart").toLocalDate();
			LocalDate DateEnd = rs.getDate("DateEnd").toLocalDate();

			Trip temp = new Trip(rs.getInt("TripID"), rs.getString("RouteID"), rs.getTime("TimeStart").toLocalTime(),
					rs.getTime("TimeEnd").toLocalTime(), DateStart, DateEnd);
			
			list.add(temp);
			
		}

		rs.close();
		psm.close();
		JDBCUtil.closeConnection(Conn);
		return list;
	}

}