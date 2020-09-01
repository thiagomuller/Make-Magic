package com.thiagomuller.hpapi;

import com.thiagomuller.hpapi.model.Character;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CharacterProvider {

	public static Character getHarry() {
		Character harry = new Character();
		harry.setName("Harry Potter");
		harry.setRole("Student");
		harry.setSchool("Hogwarts School of Witchcraft and Wizardry");
		harry.setHouseId("5a05e2b252f721a3cf2ea33f");
		harry.setPatronus("Deer");
		harry.setBloodStatus("Pure-Blood");
		return harry;
	}
	
	public static Character getHermione() {
		Character hermione = new Character();
		hermione.setName("Hermione Granger");
		hermione.setSchool("Hogwarts School of Witchcraft and Wizardry");
		hermione.setHouseId("5a05e2b252f721a3cf2ea33f");
		hermione.setPatronus("Otter");
		hermione.setRole("Student");
		hermione.setBloodStatus("Muggle-Born");
		return hermione;
	}
	
	public static Character getRony() {
		Character rony = new Character();
		rony.setName("Rony Weasley");
		rony.setSchool("Hogwarts School of Witchcraft and Wizardry");
		rony.setPatronus("Rat");
		rony.setRole("Student");
		rony.setHouseId("5a05e2b252f721a3cf2ea33f");
		rony.setBloodStatus("Pure-Blood");
		return rony;
	}
	
	public static Character getSeverus() {
		Character severus = new Character();
		severus.setName("Severus Snape");
		severus.setRole("Professor");
		severus.setPatronus("Doe");
		severus.setSchool("Hogwarts School of Witchcraft and Wizardry");
		severus.setHouseId("5a05dc8cd45bd0a11bd5e071");
		severus.setBloodStatus("Pure-Blood");
		return severus;
	}
}
