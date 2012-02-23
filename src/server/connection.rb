

class Connection < Sinatra::Request
	@@id_counter = 1
	@@connections = {}

	def id
		session[:id]
	end

	def initialize id
		if @@connections[id]
			@connections[id]
		else
			session[:id] = @@id_counter
			@@connections[id] = self
			@id_counter += 1
		end
	end

	def disconnect
		@@connections.delete id
	end
end