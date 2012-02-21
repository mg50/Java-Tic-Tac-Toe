__ :name => "tic_tac_toe"

board {
	9.times do |i|
		tic_tac_toe_button :text => "X", :id => i.to_s
	end
	
}

sidebar {
	
}