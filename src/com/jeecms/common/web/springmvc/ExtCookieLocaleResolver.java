package com.jeecms.common.web.springmvc;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.i18n.LocaleContext;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

public class ExtCookieLocaleResolver extends CookieLocaleResolver {
	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		try {
			return super.resolveLocale(request);
		} catch (Exception e) {
			return Locale.forLanguageTag("zh_CN");
		}
	}

	@Override
	public LocaleContext resolveLocaleContext(final HttpServletRequest request) {
		try {
			return super.resolveLocaleContext(request);
		} catch (Exception e) {
			return new LocaleContext() {
				@Override
				public Locale getLocale() {
					return Locale.forLanguageTag("zh_CN");
				}
			};
		}
	}
}