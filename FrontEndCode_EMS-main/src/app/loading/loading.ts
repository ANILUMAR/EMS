import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatToolbarModule } from '@angular/material/toolbar';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-loading',
  standalone: true,
  imports: [
    MatToolbarModule,
    MatButtonModule,
    RouterModule,
    MatMenuModule,
    MatIconModule,
    MatDividerModule,
    MatCardModule,
    HttpClientModule
  ],
  templateUrl: './loading.html',
  styleUrl: './loading.css'
})
export class Loading implements OnInit {
  constructor(private http: HttpClient) {}

  message = '';
  translations: { [key: string]: string } = {};

  ngOnInit(): void {
    this.loadTranslations('en'); // load default language (can be changed to 'hi', etc.)
  }

  onLanguageChange(event: any): void {
    const selectedLang = event.target.value;
    this.loadTranslations(selectedLang);
  }

  private loadTranslations(lang: string): void {
    const apiUrl = `http://localhost:9001/api/translation?lang=${lang}`;

    this.http.get<any>(apiUrl).subscribe(
      (response) => {
        let translationArray = [];

        if (Array.isArray(response)) {
          translationArray = response;
        } else if (response.translations && Array.isArray(response.translations)) {
          translationArray = response.translations;
        }

        if (translationArray.length > 0) {
          this.translations = {};
          translationArray.forEach((t: { key: string; value: string }) => {
            this.translations[t.key] = t.value;
          });
        } else {
          console.warn('Unexpected translation format:', response);
        }
      },
      (error) => {
        console.error('Error fetching translations:', error);
      }
    );
  }
}
