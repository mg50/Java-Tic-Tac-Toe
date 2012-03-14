include_class Java::javattt::strategy.HumanStrategy


class HTTPPlayer < Java::Javattt.Player
	attr_accessor :session_id, :timestamp

	@@players = {}

	def self.clear_all_players
		@@players = {}
	end

	def self.[](sess)
		@@players[sess[:session_id]] ||= HTTPPlayer.new sess[:session_id]
	end

	def initialize(session_id)
		super()
		touch
		self.ui = HTTPUI.new
		self.gameStrategy = HumanStrategy.new
		session_id = session_id
	end

	def touch
		@timestamp = Time.now.to_i
	end

	def alert_message alert_message
		self.ui.alert_message = alert_message
	end

	def wait_message wait_message
		self.ui.wait_message = wait_message
	end

	def ui_status
		{:wait_message => self.ui.wait_message, :alert_message => self.ui.alert_message}
	end
end