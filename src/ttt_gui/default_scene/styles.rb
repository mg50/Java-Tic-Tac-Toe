# This file, (styles.rb) in the root directory of the production, should define any styles that are used in multiple
# scenes.  It makes use of the StyleBuilder DSL.
#
# For more information see: http://limelightwiki.8thlight.com/index.php/A_Cook%27s_Tour_of_Limelight#Styling_with_styles.rb
# For a complete listing of style attributes see: http://limelightwiki.8thlight.com/index.php/Style_Attributes

# An example style definition
#foo {
#  width 100
#  height 100
#  background_color :blue
#}

tic_tac_toe {
	width "100%"
	height "100%"
	background_color :black
	text_color :white
}

board_container {
	padding 5
}

board_3x3 {
	width 500
	height 500

}

board_4x4 {
	width 500
	height 500

}

sidebar {
	width 200
	horizontal_alignment :center
	transparency "100%"
}

message_box {
	width "100%"
	height 50
	top_padding 20
	font_size 24
	left_padding 5
	right_padding 5
	horizontal_alignment :center	
	text_color :white	
}

button {
	horizontal_alignment :center		
	border_color "#FFFFFF"
	border_width 1
	width 70
	padding 5
	margin 10
	font_size 20
	text_color :white	
}

cell_3x3 {
	width "33%"
	height "33%"
	border_color "#FFFFFF"
	bottom_border_width 1
	top_border_width 1
	left_border_width 1
	right_border_width 1
	vertical_alignment :center
	horizontal_alignment :center
	font_size 130;
	text_color :white

}

cell_4x4 {
	width "25%"
	height "25%"
	border_color "#FFFFFF"
	bottom_border_width 1
	top_border_width 1
	left_border_width 1
	right_border_width 1
	vertical_alignment :center
	horizontal_alignment :center
	font_size 130;
	text_color :white

}