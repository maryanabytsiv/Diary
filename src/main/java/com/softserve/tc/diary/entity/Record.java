package com.softserve.tc.diary.entity;

public class Record {
		
		private int u_u_id;
		private String user_name;
		private String timestamp;
		private String text;
		private String supplement;
		private boolean visibility;
		
		public Record() {
			// TODO Auto-generated constructor stub
		}
		
		public Record(int u_u_id, String user_name, String timestamp, String text,
				String supplement, boolean visibility) {
			super();
			this.u_u_id = u_u_id;
			this.user_name = user_name;
			this.timestamp = timestamp;
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

		public String getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
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

		public boolean isVisibility() {
			return visibility;
		}

		public void setVisibility(boolean visibility) {
			this.visibility = visibility;
		}

		@Override
		public String toString() {
			return "Record [u_u_id=" + u_u_id + ", user_name=" + user_name
					+ ", timestamp=" + timestamp + ", text=" + text
					+ ", supplement=" + supplement + ", visibility=" + visibility
					+ "]";
		}

}
