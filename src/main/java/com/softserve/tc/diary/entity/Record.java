package com.softserve.tc.diary.entity;

public class Record {
		
		private String id_rec;
		private String user_name;
		private String created_time;
		private String text;
		private String supplement;
		private status visibility;
		
		public Record() {
			// TODO Auto-generated constructor stub
		}
		
		public Record(String user_name, String created_time, String text,
				String supplement, status visibility) {
			super();
			this.user_name = user_name;
			this.created_time = created_time;
			this.text = text;
			this.supplement = supplement;
			this.visibility = visibility;
		}

		public String getId_rec() {
			return id_rec;
		}

		public void setId_rec(String id_rec) {
			this.id_rec = id_rec;
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

		public String getVisibility() {
			if (visibility == status.PRIVATE) {
				return "PRIVATE";
			} else if (visibility == status.PUBLIC) {
				return "PUBLIC";
			} else
				return null;
		}

		public void setVisibility(String visibility) {
			if (visibility.equals("PRIVATE")) {
				this.visibility = status.PRIVATE;
			} else if (visibility.equals("PUBLIC")) {
				this.visibility = status.PUBLIC;
			} else
				this.visibility = null;
		}		
		
		@Override
		public String toString() {
			return "Record [id_rec=" + id_rec + ", user_name=" + user_name
					+ ", created_time=" + created_time + ", text=" + text
					+ ", supplement=" + supplement + ", visibility=" + visibility
					+ "]";
		}

}
