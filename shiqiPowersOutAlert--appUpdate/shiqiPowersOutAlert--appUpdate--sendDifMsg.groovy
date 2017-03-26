/**
 *  shiqiPowersOutAlert
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
definition(
    name: "shiqiPowersOutAlert",
    namespace: "wsq",
    author: "Yunhan Jia & Shiqi Wang",
    description: "Alert me of power loss using motion detector&#39;s change from wired-power to battery-power. SmartThings hub and internet connection must be working! You can connect the hub and internet connection device (e.g. modem, router, etc.) to a battery backup power strip so that the motion detector and detect the loss and the hub and router will still have enough power to get the message out before they fail as well.",
    category: "Safety & Security",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("When there is wired-power loss on...") {
			input "lockMotionSensor", "capability.motionSensor", title: "Where?"
            input "windowMotionSensor", "capability.motionSensor", title :"where?"
	}
	section("Via a push notification and a text message(optional)"){
    	input "pushAndPhone", "enum", title: "Send Text?", required: false, metadata: [values: ["Yes","No"]]
		input "phone1", "phone", title: "Phone Number (for Text, optional)", required: false

	}
}

def installed() {
    log.debug "installed"
	subscribe(lockMotionSensor, "powerSource.battery", onBatteryPowerHandler)
    subscribe(lockMotionSensor, "powerSource.powered", PoweredPowerHandler)
    subscribe(windowMotionSensor, "powerSource.battery", onBatteryPowerHandler)
    subscribe(windowMotionSensor, "powerSource.powered", PoweredPowerHandler)
    //subscribe(motion1, "motion", myhandler)
}

def updated() {
	unsubscribe()
    state.attack = true //every time update the smartApp, activate it.
    log.debug "updated"
	subscribe(lockMotionSensor, "powerSource.battery", onLockBatteryPowerAttackHandler)
    //subscribe(lockMotionSensor, "powerSource.battery", onLockBatteryPowerHandler)
    subscribe(lockMotionSensor, "powerSource.powered", LockPoweredPowerHandler)
    //subscribe(lockMotionSensor, "powerSource.powered", LockPoweredPowerHandler)
    subscribe(windowMotionSensor, "powerSource.battery", onWindowBatteryPowerHandler)
    subscribe(windowMotionSensor, "powerSource.powered", WindowPoweredPowerAttackHandler)
    //subscribe(motion1, "motion", myhandler)
}

def myhandler(evt) {
	log.debug "${windowMotionSensor.label ?: windowMotionSensor.name} sensed Power is Out!"
}

def onWindowBatteryPowerHandler(evt) {
	log.trace "$evt.value: $evt"
    
	def msg = "${windowMotionSensor.label ?: windowMotionSensor.name} sensed Power is Out!"
    
	log.debug "sending push for power is out"
	sendPush(msg)
    
    if ( phone1 && pushAndPhone ) {
    	log.debug "sending SMS to ${phone1}"
   	sendSms(phone1, msg)
	}
}
def onLockBatteryPowerHandler(evt) {
	log.trace "$evt.value: $evt"
    
	def msg = "${lockMotionSensor.label ?: lockMotionSensor.name} sensed Power is Out!"
    
	log.debug "sending push for power is out"
	sendPush(msg)
    
    if ( phone1 && pushAndPhone ) {
    	log.debug "sending SMS to ${phone1}"
   	sendSms(phone1, msg)
	}
}

def WindowPoweredPowerHandler(evt) {
	log.trace "$evt.value: $evt"
	def msg = "${windowMotionSensor.label ?: windowMotionSensor.name} sensed Power is Back On!"
    
	log.debug "sending push for power is back on"
	sendPush(msg)
    
    if ( phone1 && pushAndPhone ) {
    	log.debug "sending SMS to ${phone1}"
    	sendSms(phone1, msg)
	}
}

def LockPoweredPowerHandler(evt) {
	log.trace "$evt.value: $evt"
	def msg = "${lockMotionSensor.label ?: lockMotionSensor.name} sensed Power is Back On!"
    
	log.debug "sending push for power is back on"
	sendPush(msg)
    
    if ( phone1 && pushAndPhone ) {
    	log.debug "sending SMS to ${phone1}"
    	sendSms(phone1, msg)
	}
}

def WindowPoweredPowerAttackHandler(evt) {
	state.attack = false //recharge the battery of the window because of the low battery of the lock, stop alert the low battery
	log.trace "$evt.value: $evt"
	def msg = "${windowMotionSensor.label ?: windowMotionSensor.name} sensed Power is Back On!"
    
	log.debug "sending push for power is back on"
	sendPush(msg)
    
    if ( phone1 && pushAndPhone ) {
    	log.debug "sending SMS to ${phone1}"
    	sendSms(phone1, msg)
	}
}
def onLockBatteryPowerAttackHandler(evt) {
    if(state.attack) {
    	log.trace "$evt.value: $evt"
    
        def msg = "${windowMotionSensor.label ?: windowMotionSensor.name} sensed Power is Out!"

        log.debug "sending push for power is out"
        sendPush(msg)
        state.attack = true //no power of the lock then attack

        if ( phone1 && pushAndPhone ) {
            log.debug "sending SMS to ${phone1}"
        	sendSms(phone1, msg)
		}
    }
	
}


