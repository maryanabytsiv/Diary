package com.softserve.tc.diary.entity;

public class Record {
		
		private int u_u_id;
		private String user_name;
		private String created_time;
		private String text;
		private String supplement;
		private Visibility visibility;
		
		public Record() {
			// TODO Auto-generated constructor stub
		}
		
		public Record(String user_name, String created_time, String text,
				String supplement, Visibility visibility) {
			super();
			this.user_name = user_name;
			this.created_time = created_time;
			this.text = text;
			this.supplement = supplement;
			this.visibility = visibility;
		}

		public int getU_u_id() {
			return u_u_id;
		}

		public void setU_u_id(int u_u_id) {
			this.u_u_id = u_u_id;
		}

		public String getUser_name() {
			return user_name;
		}

		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}

		public String getCreated_time() {
			return created_time;
		}

		public void setCreated_time(String created_time) {
			this.created_time = created_time;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public String getSupplement() {
			return supplement;
		}

		public void setSupplement(String supplement) {
			this.supplement = supplement;
		}

		public Visibility getVisibility() {
			return visibility;
		}

		public void setVisibility(Visibility visibility) {
			this.visibility = visibility;
		}

		@Override
		public String toString() {
			return "Record [u_u_id=" + u_u_id + ", user_name=" + user_name
					+ ", created_time=" + created_time + ", text=" + text
					+ ", supplement=" + supplement + ", visibility=" + visibility
					+ "]";
		}

}
