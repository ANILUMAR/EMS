import { HttpClientModule } from '@angular/common/http';
import { Component, NgModule, ViewEncapsulation } from '@angular/core';
import { NavigationEnd, RouterOutlet } from '@angular/router';
import { Header } from "./layout/header/header";
import { Sidebar } from "./layout/sidebar/sidebar";
import { filter } from 'rxjs';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common'; // ✅ Needed for *ngIf




@Component({
  selector: 'app-root',
   standalone: true,
  imports: [RouterOutlet, HttpClientModule, Header, Sidebar, CommonModule],
  
  templateUrl: './app.html',
  styleUrl: './app.css'
    //encapsulation: ViewEncapsulation.None, // Needed for global style overrides

})


export class App {
  showHeader = true;

  constructor(private Router: Router) {
    this.Router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: any) => {
        const noHeaderRoutes = ['/','/login', '/register', '/forgotpassward'];
        this.showHeader = !noHeaderRoutes.includes(event.urlAfterRedirects);
      });
  }
}
  
  

