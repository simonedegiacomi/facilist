import { Injectable } from '@angular/core';
import { TranslateService } from "@ngx-translate/core";

const SUPPORTED_LOCALES = ['it-IT', 'en-US'];
const DEFAULT_LOCALE = 'it-IT';

@Injectable({
    providedIn: 'root'
})
export class I18nService {

    constructor(private i18n: TranslateService) {

    }

    /**
     * Call this method only once, when the app starts. This methods setup the translation library configuring the
     * available languages.
     */
    onAppSetup() {
        this.i18n.addLangs(SUPPORTED_LOCALES);
        this.i18n.setDefaultLang(DEFAULT_LOCALE);
    },

    getCurrentLocale () {
        return this.i18n.currentLang || DEFAULT_LOCALE;
    }

    setCurrentLocale (locale: string) {
        this.i18n.use(locale);
    }
}
