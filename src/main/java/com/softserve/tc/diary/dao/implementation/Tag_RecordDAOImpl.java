/*package com.softserve.tc.diary.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.softserve.tc.diary.ConnectManager;
import com.softserve.tc.diary.dao.TagDAO;
import com.softserve.tc.diary.dao.Tag_RecordDAO;
import com.softserve.tc.diary.entity.Tag;
import com.softserve.tc.diary.entity.Tag_Record;

public class Tag_RecordDAOImpl extends BaseDAOImpl<Tag_Record>implements Tag_RecordDAO {

	public Tag_Record readByKey(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	public void create(Tag_Record object) {
		try {
			Connection conn = ConnectManager.getConnection();
			PreparedStatement ps = conn.prepareStatement("INSERT INTO tag_record VALUES( ?, ?, ? )");
			ps.setInt(1, object.getU_u_id());
			ps.setInt(2, object.getRecord_tag());
			ps.setInt(3, object.getTag_uuid());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			;
		}

	}

	public void update(Tag_Record object) {
		try {
			Connection conn = ConnectManager.getConnection();
			PreparedStatement ps = conn
					.prepareStatement("UPDATE  tag_record SET record_tag= ? ,tag_uuid= ? WHERE u_u_id= ? ");
			ps.setInt(3, object.getU_u_id());
			ps.setInt(1, object.getRecord_tag());
			ps.setInt(2, object.getTag_uuid());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			;
		}

	}

	public void delete(Tag_Record object) {
		try {
			Connection conn = ConnectManager.getConnection();
			PreparedStatement ps = conn.prepareStatement("DELETE  FROM  tag_record  WHERE u_u_id= ? ");
			ps.setInt(1, object.getU_u_id());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			;
		}

	}

	public List<Tag_Record> getAll() {
		List<Tag_Record> resultList = new ArrayList<Tag_Record>();
		Connection conn = ConnectManager.getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM tag_record");
			try {
				ResultSet rs = ps.executeQuery();
				try {
					while (rs.next()) {
						Tag_Record tag_Record = transform.fromRStoObject(rs);
						resultList.add(tag_Record);
					}
					return resultList;
				} finally {
					rs.close();
				}
			} finally {
				ps.close();
			}
		} finally {
			conn.close();
		}
	}

	public Tag_Record getById(int id) {

		try {
			Connection con = ConnectManager.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM tag_record WHERE uu_id = ?");

			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			try{
			if (rs.next()) {
				Tag_Record tag_Record = new Tag_Record();
				int uuid = rs.getInt(1);
				int record_tag = rs.getInt(2);
				int tag_uuid = rs.getInt(3);
				tag_Record.setU_u_id(uuid);
				tag_Record.setRecord_tag(record_tag);
				tag_Record.setTag_uuid(tag_uuid);

				return tag_Record;
			}else
			{
				return null;
			}
			} finally {
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}

	public Tag_Record getByRecord(int recordId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Tag_Record getByTag(int tagId) {
		// TODO Auto-generated method stub
		return null;
	}

}
*/