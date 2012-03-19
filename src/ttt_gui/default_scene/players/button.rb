include_class Java::Javattt.command.YesCommand
include_class Java::Javattt.command.NoCommand

module Button
	def self.extended(prop)
	end

	def mouse_clicked(e)
		if self.id == "button_yes"
			cmd = YesCommand.new
		else
			cmd = NoCommand.new
		end

		@scene.find_by_name("sidebar")[0].children.each do |c|
			c.style.transparency = "100%"
		end
		Game.start cmd
	end
end