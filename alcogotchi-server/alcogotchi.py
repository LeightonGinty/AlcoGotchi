import json
import socket
import _thread
import time
import random
from random import randint

BEVERAGES = {"beer": 2.5, "wine": 3, "whisky": 2, "lemonade": 0}
driving = False
terminated = False
def play_music_once(melody_name):
    buzzer.melody(melody_name)
def drive(base_speed):
    screen.rect(0,0,160,128,(0, 0, 0),1)
    driving = True
    happiness = 9
    lane = 0
    driving_time = 0
    # opposing_vehicle_y = 0

    range_speed = 30

    opposing_vehicle_ys = [0,0,0]
    number_of_vehicles = 10
    base_speed *= 2.5
    base_speed = int(base_speed)
    opposing_vehicle_speeds = [randint(base_speed,base_speed+range_speed) for i in range(number_of_vehicles)]
    opposing_vehicle_lanes = [randint(0,3) for i in range(number_of_vehicles)]
    playing = True
    startup_time = 0
    for i in range(len(opposing_vehicle_ys)):
      screen.rect(13+opposing_vehicle_lanes[i]*40, opposing_vehicle_ys[i],20,40,(500, 500, 555),1)
    def play_music(CHASE):
      while driving:
        buzzer.melody(CHASE)
    CHASE = "a4:1 b c5 b4 a:2 r a:1 b c5 b4 a:2 r a:2 e5 d# e f e d# e b4:1 c5 d c b4:2 r b:1 c5 d c b4:2 r b:2 e5 d# e f e d# e "
      


    new_thread = _thread.start_new_thread(play_music, [CHASE])
    while playing and driving:
      driving_time += 1
      if driving_time > 100:
        break
      
      # WAWA = "e3:3 r:1 d#:3 r:1 d:4 r:1 c#:8 "

      startup_time += 1
      if startup_time >= 10:
        for i in range(len(opposing_vehicle_ys)):
          opposing_vehicle_ys[i]+=opposing_vehicle_speeds[i]
          if  80 <= (opposing_vehicle_ys[i]+40) <= 120  and opposing_vehicle_lanes[i] == lane:
            print("GAME OVER")
            playing = False
          if opposing_vehicle_ys[i] > 180:
            opposing_vehicle_ys[i] = 0
            opposing_vehicle_lanes[i] = randint(0,3)
          screen.rect(13+opposing_vehicle_lanes[i]*40, opposing_vehicle_ys[i]-opposing_vehicle_speeds[i],20,40,(0, 0,0),1)
      
          screen.rect(13+opposing_vehicle_lanes[i]*40, opposing_vehicle_ys[i],20,40,(500, 500, 555),1)
          
      for i in range(10):
        screen.rect(80,i*15,5,10,(500-50*i, 50*i, 555),1)
      for i in range(10):
        screen.rect(40,i*15,5,10,(500-50*i, 50*i, 555),1)
      for i in range(10):
        screen.rect(120,i*15,5,10,(500-50*i, 50*i, 555),1)
      if sensor.btnValue("a"):
        lane -= 1
        if lane < 0:
          lane = 0
      if sensor.btnValue("b"):
        lane += 1
        if lane > 3:
          lane = 3

      if lane == 0:
        screen.rect(13,80,20,40,(500, 500, 555),1)
        screen.rect(53,80,20,40,(0, 0, 0),1)
        screen.rect(93,80,20,40,(0, 0, 0),1)
        screen.rect(133,80,20,40,(0, 0, 0),1)
      if lane == 1:
        screen.rect(53,80,20,40,(500, 500, 555),1)
        screen.rect(13,80,20,40,(0, 0, 0),1)
        screen.rect(93,80,20,40,(0, 0, 0),1)
        screen.rect(133,80,20,40,(0, 0, 0),1)
      if lane == 2:
        screen.rect(93,80,20,40,(500, 500, 555),1)
        screen.rect(33,80,20,40,(0, 0, 0),1)
        screen.rect(53,80,20,40,(0, 0, 0),1)
        screen.rect(133,80,20,40,(0, 0, 0),1)
      if lane == 3:
        screen.rect(133,80,20,40,(500, 500, 555),1)
        screen.rect(13,80,20,40,(0, 0, 0),1)
        screen.rect(53,80,20,40,(0, 0, 0),1)
        screen.rect(93,80,20,40,(0, 0, 0),1)

      screen.refresh()
    else:
      screen.rect(0,0,160,128,(0, 0, 0),1)
      driving = False
      return True
    screen.text("GAME OVER", x=10, y=90, ext=1, color=255)
    driving = False
    screen.rect(0,0,160,128,(0, 0, 0),1)
    return False
    
def s():
    #screen.rect(0,0,160,128,(0, 0, 0),1)
    if not driving:
      drunkness = alcogotchi.drunk
      happiness = alcogotchi.happiness
      health = alcogotchi.health
      screen.loadPng('alcogotchi.png')
      
      screen.text("Coin: {0}".format(alcogotchi.alco_coin), x=10, y=50, ext=1, color=255)
      screen.text("Health", x=10, y=60, ext=1, color=255)
      #screen.rect(10,100,150,10,(0, 0, 0),1)
      screen.rect(10,70,int(health*1.2),10,(255-int(2.55*health), int(2.55*health), 0),1)
      screen.text("Drunkness", x=10, y=80, ext=1, color=255)
      #screen.rect(10,100,150,10,(0, 0, 0),1)
      screen.rect(10,90,int(drunkness*1.2),10,(255-int(2.55*drunkness), int(2.55*drunkness), 0),1)
      screen.text("Happiness", x=10, y=100, ext=1, color=255)
      
      #screen.rect(10,100,150,10,(0, 0, 0),1)
      screen.rect(10,110,int(happiness*1.2),10,(255-int(2.55*happiness), int(2.55*happiness), 0),1)

class Alcogotchi:
    def __init__(self, name):
        self.name = name
        self.last_drunkentime = time.time()
        self.drunk = 0
        self.happiness = 50
        self.health = 100
        self.alco_coin = 100
        self.items = {"beer": 0, "wine": 0, "whisky": 0, "lemonade": 0}

    def get_alcogotchi(self):
        return self.__dict__

    def double_or_nothing(self, data):
        BA_DING = "b5:1 e6:3 "
        ERROR = "a3:2 r a3:2 "


        bet = dict(data)["bet"]
        if self.alco_coin >= bet:
            if random.randint(0,1) == 0:
              self.alco_coin += bet
              self.happiness += 2
              new_thread = _thread.start_new_thread(play_music_once, [BA_DING])
            else:
              self.alco_coin -= bet
              self.happiness -= 2
              new_thread = _thread.start_new_thread(play_music_once, [ERROR])

            s()
            return self.get_alcogotchi()
        s()
        raise Exception("You got no coin")
        
    def buy_item(self, data):
        item = dict(data)["item"]
        if item in self.items and self.alco_coin > 3:
            self.alco_coin -= 3
            self.items[item] += 1
            s()
            return self.get_alcogotchi()
        raise Exception("Item doesn't exist")
        
    def drink(self, data):
        global terminated
        drink_choice = data["drink"]
        if self.last_drunkentime + 8 < time.time() and self.items[drink_choice] > 0:
            self.drunk += BEVERAGES[drink_choice]
            self.happiness += BEVERAGES[drink_choice]
            self.last_drunkentime = time.time()
            self.items[drink_choice] -= 1
            self.health -= 1
            if self.health <1:
              terminated = True
        self.drunk = min(100, self.drunk)
        self.happiness = min(100, self.happiness)
        s()
        return self.get_alcogotchi()

    def drive(self):
        global terminated
        crash = drive(int(self.drunk))
        if crash:
          self.health -= 20
          self.happiness -= 10
          if self.health == 0:
            terminated = True
          self.happiness = max(self.happiness, 0)
        else:
          self.happiness += 10
          self.happiness = min(100, self.happiness)
        s()
        return self.get_alcogotchi()
        
    def mine(self):
        if self.happiness > 0:
            self.happiness -= 1
            self.alco_coin += 2
            s()
            return self.get_alcogotchi()
        self.items["beer"] += 1
        s()
        raise Exception("You're too sad to mine coal. Here have a beer :)")
    
alcogotchi = Alcogotchi("Brian")

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
        global terminated
        print("Server running on port {0}...".format(self.port))
        while not terminated:
            self.connection, client_address = self._sock.accept()
            try:
                request = self.connection.recv(1024).decode().split("\r\n")
                method, route, _ = request[0].split(" ")
                print("{0} {1} on :{2} from {3}".format(method, route, self.port, client_address[0]))

                if route in self._routes:
                    if method == "GET":
                        self._send(self._routes[route]())
                    if method == "POST":
                        data = json.loads(request[-1])
                        # data as argument
                        self._send(self._routes[route](data))
                else:
                    self._send("Route is not defined", 404)

            except Exception as e:
                print("Error: {0}".format(e))
                self._send(str(e), 500)

            finally:
                self.connection.close()
      
ap = network.WLAN(network.AP_IF)
ap.active(True)
ap.config(essid="Cool Server1", password="password123")
print("Connection avalible on {0}".format(ap.ifconfig()))

server = Server()

server.add_route("/", alcogotchi.get_alcogotchi)
server.add_route("/drink", alcogotchi.drink)
server.add_route("/gamble", alcogotchi.double_or_nothing)
server.add_route("/buy", alcogotchi.buy_item)
server.add_route("/drive", alcogotchi.drive)
server.add_route("/mine", alcogotchi.mine)
# _thread.start_new_thread(s, ())
s()
server.start()
_thread.exit()
FUNERAL = "c3:4 c:3 c:1 c:4 d#:3 d:1 d:3 c:1 c:3 b2:1 c3:4 "
buzzer.melody(FUNERAL)
