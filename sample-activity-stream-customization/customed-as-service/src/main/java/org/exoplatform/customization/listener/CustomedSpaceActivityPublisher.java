/*
 * Copyright (C) 2003-2014 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU Affero General Public License
* as published by the Free Software Foundation; either version 3
* of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.customization.listener;

import org.exoplatform.social.core.application.SpaceActivityPublisher;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.social.core.manager.ActivityManager;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.space.spi.SpaceLifeCycleEvent;

/**
 * Created by The eXo Platform SAS
 * Mar 13, 2014  
 */
/**
 * This listener override org.exoplatform.social.core.space.spi.SpaceLifeCycleEvent
 * to don't update activities when user join/left space (ORG-625)
 *
 */
public class CustomedSpaceActivityPublisher extends SpaceActivityPublisher {
  public CustomedSpaceActivityPublisher(InitParams params,
      ActivityManager activityManager, IdentityManager identityManager) {
    super(params, activityManager, identityManager);
    // TODO Auto-generated constructor stub
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void joined(SpaceLifeCycleEvent event) {
   //Do nothing
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void left(SpaceLifeCycleEvent event) {
    //Do nothing
  }
}
