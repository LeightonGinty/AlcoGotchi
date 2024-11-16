import json
import socket
import _thread

class Alcogotchi:
    def __init__(self, name):
        self.name = name
        self.drunk = 0
        self.health = 100
        self.alco_coin = 0

    def __str__(self):
        return self.__dict__
        
    def __repr__(self):
      return self.__dict__

class Server:
    HTTP_CODES = {
        200: "OK",
        404: "Not Found",
        500: "Internal Server Error",
    }

    def __init__(self, port=80):
        self.port = port
        self.connection = None
        self._sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self._sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self._sock.bind(("", port))
        self._sock.listen(1)
        self._routes = {}

    def add_route(self, path, callback, nowait=False):
        if nowait:
            self._routes[path] = lambda: (_thread.start_new_thread(callback, ()), None)[1]
        else:
            self._routes[path] = callback

    def _send(self, data, code=200):
        if self.connection is not None:
            content = json.dumps({"data" if code == 200 else "error": data})
            self.connection.send(
                "HTTP/1.1 {0} {2}\r\nContent-Type: application/json\r\n\r\n{2}".format(code, self.HTTP_CODES[code], content).encode()
            )

    def start(self):
        print("Server running on port {self.port}...")
        while True:
            self.connection, client_address = self._sock.accept()
            try:
                request = self.connection.recv(1024).decode().split("\r\n")
                method, route, _ = request[0].split(" ")
                print("{0} {1} on :{2} from {3}".format(method, route, self.port, client_address[0]))

                if route in self._routes:
                    self._send(self._routes[route]())
                else:
                    self._send("Route is not defined", 404)

            except Exception as e:
                print("Error: {0}".format(e))
                self._send(str(e), 500)

            finally:
                self.connection.close()

ap = network.WLAN(network.AP_IF)
ap.active(True)
ap.config(essid="Cool Server", password="password123")
print("Connection avalible on {0}".format(ap.ifconfig()))
    

server = Server()
alcogotchi = Alcogotchi("Brian")
server.add_route("/text", lambda: "Hello World!")

def get_alcogotchi():
    return alcogotchi.__dict__

def beer():
    alcogotchi.drunk += 1
    return get_alcogotchi
    
server.add_route("/brian", get_alcogotchi)
server.add_route("/beer", beer)
server.start()
