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
			if(pstmt != null) pstmt.close();
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
		String sql = "insert into mvc_board values(mvc_board_seq.nextval,?,?,?,sysdate,?,mvc_board_seq.currval,0,0)";
		System.out.println("BDao write sql-->"+sql);
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
		upHit(bId);
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

	private void upHit(int bId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "update mvc_board set bhit=bhit+1 where bid=?";
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bId);
			pstmt.executeUpdate();
		} catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			close(conn, pstmt, null);
		}
		
	}

	@Override
	public void modify(int bId, String bName, String bTitle, String bContent) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "update mvc_board set bname=?, btitle=?, bcontent=? where bid=?";
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bName);
			pstmt.setString(2, bTitle);
			pstmt.setString(3, bContent);
			pstmt.setInt(4, bId);
			pstmt.executeUpdate();
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			close(conn, pstmt, null);
		}
		
	}

	@Override
	public BDto reply_view(int bId) {
		BDto dto = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from mvc_board where bid=?";
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bId);
			System.out.println("BDao sql->"+sql);
			rs = pstmt.executeQuery();
			
				
			if(rs.next()) {
					int id = rs.getInt("bId");
					String bName = rs.getString("bName");
					String bTitle = rs.getString("bTitle");
					String bContent = rs.getString("bContent");
					Timestamp bDate = rs.getTimestamp("bDate");
					int bHit = rs.getInt("bHit");
					int bGroup = rs.getInt("bGroup");
					int bStep = rs.getInt("bStep");
					int bIndent = rs.getInt("bIndent");
					dto = new BDto(id, bName, bTitle, bContent, bDate, bHit, bGroup, bStep, bIndent);
			}
		
		
		} catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			close(conn, pstmt, rs);
		}
		return dto;
	}

	@Override
	public void reply(int bId, String bName, String bTitle, String bContent, int bGroup, int bStep, int bIndent) {
		replyShape(bGroup, bStep);
		Connection conn = null;
		PreparedStatement pstmt = null;
		String inSql = "insert into mvc_board values(mvc_board_seq.nextval,?,?,?,sysdate,0,?,?,?)";
		// String sql = "update mvc_board set bstep=bstep+1 where bgroup=? and bstep>?";
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(inSql);
			pstmt.setString(1, bName);
			pstmt.setString(2, bTitle);
			pstmt.setString(3, bContent);
			pstmt.setInt(4, bGroup);
			pstmt.setInt(5, bStep+1);
			pstmt.setInt(6, bIndent+1);
			pstmt.executeUpdate();
		} catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			close(conn, pstmt, null);
		}
		
	}

	private void replyShape(int bGroup, int bStep) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			String sql = "update mvc_board set bstep=bstep+1"
						+ " where bgroup=? and bstep>?";
			pstmt= conn.prepareStatement(sql);
			pstmt.setInt(1, bGroup);
			pstmt.setInt(2, bStep);
			pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, pstmt, null);
		}
		
	}

	@Override
	public void delete(int bId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			String sql = "delete from mvc_board where bid=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bId);
			pstmt.executeUpdate();
		} catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			close(conn, pstmt, null);
		}
		
	}
	
}
