package test.net.jplugin.core.das.hib;

import net.jplugin.core.das.hib.api.Entity;

@Entity(idgen="uuid.hex")
public class DBHibTest {

		String id;
		String name;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
}
