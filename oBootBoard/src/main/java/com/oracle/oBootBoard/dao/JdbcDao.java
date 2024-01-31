package com.oracle.oBootBoard.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;

import com.oracle.oBootBoard.dto.BDto;

public class JdbcDao implements BDao {
	
	// JDBC 사용
	private final DataSource dataSource;
	
	public JdbcDao(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	private Connection getConnection() {
		return DataSourceUtils.getConnection(dataSource);
	}

	@Override
	public ArrayList<BDto> boardList() {
		ArrayList<BDto> bList = new ArrayList<BDto>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		System.out.println("list start..");
		ResultSet resultSet = null;
		String sql = "select bid, bname, btitle, bcontent, bdate, bhit, bgroup, bstep, bindent from mvc_board order by bgroup desc, bstep asc";
		try {
			connection = getConnection();
			preparedStatement = connection.prepareStatement(sql);
			System.out.println("BDao sql->"+sql);
			resultSet = preparedStatement.executeQuery();
			
				
				while(resultSet.next()) {
					int bId = resultSet.getInt("bId");
					String bName = resultSet.getString("bName");
					String bTitle = resultSet.getString("bTitle");
					String bContent = resultSet.getString("bContent");
					Timestamp bDate = resultSet.getTimestamp("bDate");
					int bHit = resultSet.getInt("bHit");
					int bGroup = resultSet.getInt("bGroup");
					int bStep = resultSet.getInt("bStep");
					int bIndent = resultSet.getInt("bIndent");
					BDto dto = new BDto(bId, bName, bTitle, bContent, bDate, bHit, bGroup, bStep, bIndent);
					bList.add(dto);
				} 
			
		} catch(Exception e) {
			System.out.println("list dataSource-->"+e.getMessage());
			e.printStackTrace();
		} finally {
			close(connection, preparedStatement, resultSet);
		}
		
		return bList;
	}
	
	private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		try {
			if(rs != null) rs.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		try {
			if(pstmt != null) pstmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		try {
			if(conn != null) close(conn);
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	private void close(Connection conn) throws SQLException {
		DataSourceUtils.releaseConnection(conn, dataSource);	// 데이터베이스 연결을 해제하는데 사용
	}

	@Override
	public void write(String bName, String bTitle, String bContent) {
		// 1. insert into mvc_board
		// 2. prepareStatement
		// 3. sequence이용
		// 4. bId , bGroup 같게
		// 5.  bStep, bIndent, bDate --> 0, 0 , sysdate
		// HW1
		String sql = "insert into mvc_board values(mvc_board_seq.nextval,?,?,?,sysdate,?,mvc_board_seq.nextval,0,0)";
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bName);
			pstmt.setString(2, bTitle);
			pstmt.setString(3, bContent);
			pstmt.setInt(4, new BDto().getbHit());
			
			result = pstmt.executeUpdate();
		} catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			close(conn, pstmt, null);
		}
	}

	@Override
	public BDto contentView(int bId) {
		BDto board = new BDto();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select bhit, bname, btitle, bcontent from mvc_board where bid=?";
		try {
			
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bId);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				
				board.setbId(bId);
				board.setbHit(rs.getInt("bhit"));
				board.setbName(rs.getString("bname"));
				board.setbTitle(rs.getString("btitle"));
				board.setbContent(rs.getString("bcontent"));
				
			} 
		} catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			close(conn, pstmt, rs);
		}
		return board;
	}

}
