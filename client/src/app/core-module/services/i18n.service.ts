import { Injectable } from '@angular/core';
import { TranslateService } from "@ngx-translate/core";
import { AuthService } from "./auth.service";

const SUPPORTED_LOCALES = ['it-IT', 'en-US'];
const DEFAULT_LOCALE    = 'it-IT';

/**
 * Service that decide which locale to use for internationalization
 */
@Injectable({
    providedIn: 'root'
})
export class I18nService {

    constructor(
        private i18n: TranslateService,
        private authService: AuthService
    ) { }

    /**
     * Call this method only once, when the app starts. This methods setup the translation library configuring the
     * available languages.
     */
    onAppSetup() {
        this.setupTranslationLibrary();

        this.useLastUsedLocaleOrDefault();

        this.listenToUserLoginEvent();
    }

    /**
     * Configure the internationalization library which locale the app supports and which is the dafult one
     */
    private setupTranslationLibrary() {
        this.i18n.addLangs(SUPPORTED_LOCALES);
        this.i18n.setDefaultLang(DEFAULT_LOCALE);
    }

    /**
     * If the locale has been saved in the local storage load it, otherwise use the default one
     */
    private useLastUsedLocaleOrDefault () {
        const latestOrDefault = window.localStorage.getItem('latestLocale') || DEFAULT_LOCALE;
        this.setCurrentLocale(latestOrDefault);
    }

    /**
     * Listen for the event emit when the user logins. This because the user locale may be different from the currently used one
     */
    private listenToUserLoginEvent() {
        this.authService.user$.subscribe(user => {
            if (user != null) {
                this.setCurrentLocale(user.locale)
            }
        });
    }

    getCurrentLocale() {
        return this.i18n.currentLang || DEFAULT_LOCALE;
    }

    setCurrentLocale(locale: string) {
        console.log(`[i18n] Switched to ${locale}`);
        this.i18n.use(locale);

        this.storeLastUsedLocale(locale);
    }

    /**
     * Store the specified locale as the last one used in the local storage
     * @param locale
     */
    private storeLastUsedLocale(locale: string) {
        window.localStorage.setItem('latestLocale', locale);
    }
}
