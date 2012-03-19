__ :name => "tic_tac_toe"

board_container {
	board_3x3 {
		9.times do |i|
			cell_3x3 :text => " ", :id => i.to_s + "_3x3"
		end
	}

	board_4x4 {
		16.times do |i|
			cell_4x4 :text => " ", :id => i.to_s + "_4x4"
		end
	}
}

# board_4x4 {
# 	16.times do |i|
# 		cell_4x4 :text => " ", :id => i.to_s + "_4x4"
# }

sidebar {
	message_box :text => "", :id => "message_prompt"
	button :text => "Yes", :id => "button_yes"
	button :text => "No", :id => "button_no"
}