/**
 *  CO Detector
 *
 *  Copyright 2016 Yunhan Jia &amp; Shiqi Wang
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
metadata {
	definition (name: "Simulated COSmoke Alarm", namespace: "wsq", author: "Shiqi Wang") {
		capability "carbonMonoxideDetector"
		capability "Sensor"

        command "COSmoke"
        command "test"
        command "clear"
	}

	simulator {

	}

	tiles {
		standardTile("main", "device.COSmoke", width: 2, height: 2) {
			state("clear", label:"clear", icon:"st.alarm.COSmoke.clear", backgroundColor:"#ffffff", action:"COSmoke")
			state("detected", label:"COSmoke!", icon:"st.alarm.COSmoke.COSmoke", backgroundColor:"#e86d13", action:"clear")
			state("tested", label:"Test", icon:"st.alarm.COSmoke.test", backgroundColor:"#e86d13", action:"clear")
		}
 		standardTile("COSmoke", "device.COSmoke", inactiveLabel: false, decoration: "flat") {
			state "default", label:'COSmoke', action:"COSmoke"
		}  
 		standardTile("test", "device.COSmoke", inactiveLabel: false, decoration: "flat") {
			state "default", label:'Test', action:"test"
		}
 		standardTile("reset", "device.COSmoke", inactiveLabel: false, decoration: "flat") {
			state "default", label:'clear', action:"clear"
		}
        main "main"
		details(["main", "COSmoke", "test", "clear"])
	}
}

def parse(String description) {
	
}

def COSmoke() {
	log.debug "COSmoke()"
	sendEvent(name: "COSmoke", value: "detected", descriptionText: "$device.displayName COSmoke detected!")
}

def test() {
	log.debug "test()"
	sendEvent(name: "COSmoke", value: "tested", descriptionText: "$device.displayName tested")
}

def clear() {
	log.debug "clear()"
	sendEvent(name: "COSmoke", value: "clear", descriptionText: "$device.displayName clear")
}
