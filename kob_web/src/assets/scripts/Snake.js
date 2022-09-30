import { GameObject  } from "./GameObject";
import { Cell } from "./Cell";

export class Snake extends GameObject{
    constructor(info, gamemap) {
        super();

        this.id = info.id;
        this.color = info.color;
        this.gamemap = gamemap;
        
        this.cells = [new Cell(info.r, info.c)];//存放蛇的身体，index=0为蛇头
        this.next_cell = null;
        this.step = 0;

        this.speed = 5;//蛇每秒走5个格子
        this.direction = -1; //-1表示无指令， 0 1 2 3表示上右下左的移动方向
        this.status = "idle"; //静止状态 move表示移动 die表示死亡

        this.dr = [1, 0, -1, 0];//行偏移量
        this.dc = [0, 1, 0, -1];//列偏移量
    }

    start() {

    }

    set_direction(d) {
        this.direction = d;
    }

    next_step() {
        const d = this.direction;
        this.next_cell = new Cell(this.cells[0].r + this.dr[d], this.cells[0].c + this.dc[d]);
        this.direction = -1;
        this.status = "move";
        this.step += 1;

        
    }

    update_move() {
        
    }

    update() {
        if(this.status === "move"){
            this.update_move();
        }
        this.render();
    }

    render() {
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;

        ctx.fillStyle = this.color;
        for(const cell of this.cells){
            ctx.beginPath();
            ctx.arc(cell.x * L, cell.y * L, L / 2, 0, Math.PI * 2);
            ctx.fill();
        }
    }
}