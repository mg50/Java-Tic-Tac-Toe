
class HTTPPlayer < Java::Javattt.Player
	attr_accessor :session_id

	@@players = {}

	def self.[](sess)
		@@players[sess[:session_id]] ||= HTTPPlayer.new sess[:session_id]
	end

	def initialize(session_id)
		touch
		self.ui = HTTPUI.new
		self.session_id = session_id
	end

	def touch
		@timestamp = Time.now.to_i
	end
end