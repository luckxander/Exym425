Feature: EXYM-425 As Clinician Natalie Jones, 
         I want to see all of my notes that need to be completed, 
         so I can decide what to work on first.

		 Background
			Given I am the clinician Natalie Jones: 
			When I view the vNext homepage, 
			I want to see my notes that need to be completed, 
			so I can complete them.
	
	
	Scenario: Display in the notes table a column with a header of 'TITLE'
		Given I am a clinician user
		 When I go to the main page Exym vNext portal
		 Then I should see in the notes table a column with a header of 'Title'	 
		 And  I should see all notes to complete under the 'Title' column
		 And  I should see notes as their 'activity name'
		 And  I should not see any notes that have a status of 'completed'
		 And if I click on the arrow I should see that the notes are sorted by first letter of first word of activity name