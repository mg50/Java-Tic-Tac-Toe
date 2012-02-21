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
	background_color :white
}

board {
	width 500
	height 500

}

sidebar {
	width "20%"
}

tic_tac_toe_button {
	width "33%"
	height "33%"
	bottom_border_color "#000000"
	bottom_border_width 1
	top_border_width 1
	left_border_width 1
	right_border_width 1
	vertical_alignment :center
	horizontal_alignment :center
	font_size 130;
}