const GAME_OBJECT = [];

export class GameObject{
    constructor() {
        GAME_OBJECT.push(this);
        this.has_called_start = false;
        this.timedelta = 0;
    }

    start() {

    }

    update() { 

    }

    on_destory() {

    }

    destory() {
        this.on_destory();

        for(let i in GAME_OBJECT){
            const obj = GAME_OBJECT[i];
            if(obj === this){
                GAME_OBJECT.splice(i);
                break; 
            }
        }
    }
}

let last_timestep;
const step = timestamp => {
    for(let obj of GAME_OBJECT){
        if(!obj.has_called_start){
            obj.has_called_start = true;
            obj.start();
        }
        else{
            obj.timedelta = timestamp - last_timestep;
            obj.update();
        }
    }
    last_timestep = timestamp;
    requestAnimationFrame(step);
}
requestAnimationFrame(step);